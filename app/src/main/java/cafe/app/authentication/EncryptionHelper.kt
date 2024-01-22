@file:Suppress("KDocUnresolvedReference")

package cafe.app.authentication

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64

/**
 * EncryptionHelper
 * Description: Utility class for encrypting and decrypting data using Android's Keystore and AES/GCM encryption.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [KEYSTORE_NAME]: The name of the Android Keystore.
 * - [KEY_ALIAS]: The alias for the encryption key stored in the Keystore.
 * - [TRANSFORMATION]: The encryption transformation to be used (e.g., "AES/GCM/NoPadding").
 * - [KEY_SIZE]: The size of the encryption key in bits.
 * - [keyStore]: The Android Keystore instance.
 *
 * [Methods]
 * - [generateSecretKey]: Generates a secret encryption key and stores it in the Keystore if not already present.
 * - [getSecretKey]: Retrieves the secret encryption key from the Keystore.
 * - [encrypt]: Encrypts a given input string and returns the encrypted data as a Base64-encoded string.
 * - [decrypt]: Decrypts a given Base64-encoded encrypted string and returns the original input string.
 */
object EncryptionHelper {

    private const val KEYSTORE_NAME = "AndroidKeyStore"
    private const val KEY_ALIAS = "myKeyAlias"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEY_SIZE = 256
    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_NAME).apply { load(null) }

    init {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateSecretKey()
        }
    }

    /**
     * generateSecretKey
     * Description: Generates a secret encryption key and stores it in the Keystore if not already present.
     */
    private fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_NAME)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    /**
     * getSecretKey
     * Description: Retrieves the secret encryption key from the Keystore.
     */
    private fun getSecretKey(): SecretKey {
        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }

    /**
     * encrypt
     * Description: Encrypts a given input string and returns the encrypted data as a Base64-encoded string.
     *
     * [Parameters/Arguments]
     * - [input]: The input string to be encrypted.
     *
     * [Return Value]
     * Description of the return value (if applicable).
     */
    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    /**
     * decrypt
     * Description: Decrypts a given Base64-encoded encrypted string and returns the original input string.
     *
     * [Parameters/Arguments]
     * - [encrypted]: The Base64-encoded encrypted string.
     *
     * [Return Value]
     * Description of the return value (if applicable).
     */
    fun decrypt(encrypted: String): String {
        val encryptedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val iv = encryptedBytes.sliceArray(0 until 12)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        val decrypted = cipher.doFinal(encryptedBytes.sliceArray(12 until encryptedBytes.size))
        return String(decrypted, Charsets.UTF_8)
    }
}
