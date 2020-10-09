package me.alberto.agrofarm.screens.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.signin_activity.*
import me.alberto.agrofarm.BuildConfig
import me.alberto.agrofarm.R
import me.alberto.agrofarm.databinding.SigninActivityBinding
import me.alberto.agrofarm.util.crypto.BiometricHelper
import javax.inject.Inject


class SignInActivity : AppCompatActivity(), BiometricHelper.BiometricAuthListener {

    @Inject
    lateinit var biometricHelper: BiometricHelper

    private lateinit var viewModel: SignInViewModel


    private lateinit var binding: SigninActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.signin_activity)

        viewModel =
            ViewModelProvider(this, SignInViewModel.Factorty()).get(SignInViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initializations()
        observe()
        setClicks()

    }

    private fun initializations() {
        biometricHelper.setListener(this)
    }

    private fun setClicks() {
        biometric_sign_in.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun showBiometricPrompt() {

    }


    private fun observe() {
        viewModel.emptyEmail.observe(this, Observer {
            it ?: return@Observer
            if (it) binding.emailInput.error = "Required field"
        })

        viewModel.emptyPassword.observe(this, Observer {
            it ?: return@Observer
            if (it) binding.passwordInput.error = "Required field"
        })

        viewModel.validCredentials.observe(this, Observer {
            it ?: return@Observer
            if (it) {
                biometricHelper.authenticateToEncrypt()
            } else {
                binding.passwordField.error = "Invalid email or password"
            }
        })
    }

    override fun onSuccess(cryptoObject: BiometricPrompt.CryptoObject?) {
        val password = binding.passwordInput.text.toString()
        biometricHelper.processData(cryptoObject, password)
    }

    companion object {
        private const val SECRET_KEY_NAME = BuildConfig.APPLICATION_ID + "secrete_name_key"
    }
}
