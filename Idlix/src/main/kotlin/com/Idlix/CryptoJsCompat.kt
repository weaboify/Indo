package com.Idlix

import com.lagradost.cloudstream3.base64Decode
import com.lagradost.cloudstream3.base64Encode
import com.lagradost.cloudstream3.utils.AppUtils
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets

object CryptoJsCompat {
    private const val AES_CIPHER = "AES/CBC/PKCS5Padding"
    private const val MD5 = "MD5"
    private const val SALT_LENGTH = 8
    private const val KEY_ITERATIONS = 3

    fun decrypt(jsonData: String, password: String): String? {
        val parsed = AppUtils.tryParseJson<Map<String, String>>(jsonData) ?: return null
        val ct = parsed["ct"] ?: return null
        val iv = parsed["iv"]?.hexToByteArray() ?: return null
        val salt = parsed["s"]?.hexToByteArray() ?: return null
        
        val (key, _) = generateKeyAndIv(password, salt)
        
        return try {
            val cipher = Cipher.getInstance(AES_CIPHER).apply {
                init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            }
            String(cipher.doFinal(base64Decode(ct)), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }

    private fun generateKeyAndIv(password: String, salt: ByteArray): Pair<ByteArray, ByteArray> {
        var dx = byteArrayOf()
        val passwordBytes = password.toByteArray(StandardCharsets.UTF_8)
        val salted = mutableListOf<Byte>()

        repeat(KEY_ITERATIONS) {
            dx = MessageDigest.getInstance(MD5).digest(dx + passwordBytes + salt)
            salted += dx.toList()
        }

        return Pair(
            salted.take(32).toByteArray(),
            salted.drop(32).take(16).toByteArray()
        )
    }

    private fun String.hexToByteArray(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}