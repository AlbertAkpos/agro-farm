package me.alberto.agrofarm.util.crypto

import javax.crypto.Cipher

interface CryptographyManager {
    fun getInitializedCipherForEncryption(keyName: String): Cipher
    fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray): Cipher
    fun encryptData(plaintext: String, cipher: Cipher): EncryptedData
    fun decryptData(cipherText: ByteArray, cipher: Cipher): String
}