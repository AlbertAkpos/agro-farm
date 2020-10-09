package me.alberto.agrofarm.util.crypto

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import me.alberto.agrofarm.BuildConfig
import java.nio.charset.Charset


class BiometricHelper(
    private val context: AppCompatActivity,
    private val cryptographyManager: CryptographyManager
) {
    private var readyToEncrypt = false
    private lateinit var ciphertext: ByteArray
    private lateinit var initializationVector: ByteArray

    private val biometricPrompt: BiometricPrompt
        get() = createBiometricPrompt()

    private val promptInfo: BiometricPrompt.PromptInfo
        get() = createPromptInfo()

    private var listener: BiometricAuthListener? = null

    fun setListener(listener: BiometricAuthListener) {
        this.listener = listener
    }


    fun authenticateToEncrypt() {
        readyToEncrypt = true
        if (BiometricManager.from(context.applicationContext)
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            val cipher =
                cryptographyManager.getInitializedCipherForEncryption(BuildConfig.SECRET_KEY_NAME)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    fun authenticateToDecrypte() {
        readyToEncrypt = false
        if (BiometricManager.from(context.applicationContext)
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                BuildConfig.SECRET_KEY_NAME,
                initializationVector
            )
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }


    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Finger Print Authentication")
            .setTitle("Login to get access")
            .setNegativeButtonText("Use password")
            .build()
        return promptInfo
    }


    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "$errorCode::$errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication failed for unknown reasons")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "Auth Successful")
                listener?.onSuccess(result.cryptoObject)
            }
        }

        return BiometricPrompt(context, executor, callback)
    }

    fun processData(cryptoObject: BiometricPrompt.CryptoObject?, text: String): String {
        return if (readyToEncrypt) {
            val encryptedData = cryptographyManager.encryptData(text, cryptoObject?.cipher!!)
            ciphertext = encryptedData.cipherText
            initializationVector = encryptedData.initializationVector
            String(ciphertext, Charset.forName("UTF-8"))
        } else {
            cryptographyManager.decryptData(ciphertext, cryptoObject?.cipher!!)
        }
    }

    interface BiometricAuthListener {
        fun onSuccess(cryptoObject: BiometricPrompt.CryptoObject?)
    }


    companion object {
        private const val TAG = "BiometricHelper"
    }
}