package com.Idlix

import org.json.JSONObject
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.util.Base64

object CryptoJsAes {
    fun decrypt(jsonStr: String, passphrase: String): String {
        val jsonData = JSONObject(jsonStr)
        val salt = jsonData.getString("s").hexStringToByteArray()
        val iv = jsonData.getString("iv").hexStringToByteArray()
        val ct = Base64.getDecoder().decode(jsonData.getString("ct"))

        val concatenatedPassphrase = passphrase.toByteArray() + salt
        var result = MessageDigest.getInstance("MD5").digest(concatenatedPassphrase)
        for (i in 1 until 3) {
            result += MessageDigest.getInstance("MD5").digest(result + concatenatedPassphrase)
        }
        val key = result.copyOfRange(0, 32)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
        return String(cipher.doFinal(ct))
    }

    fun dec(r: String, e: String): String {
        val rList = r.split("\\x").drop(1).map { it.take(2).toInt(16) }
        val mPadded = addBase64Padding(e.reversed())
        return try {
            val decodedM = String(Base64.getDecoder().decode(mPadded))
            decodedM.split("|")
                .filter { it.toIntOrNull()?.let { idx -> idx < rList.size } == true }
                .joinToString("") { "\\x%02x".format(rList[it.toInt()]) }
        } catch (e: Exception) {
            ""
        }
    }

    private fun addBase64Padding(b64String: String): String {
        return b64String + "=".repeat((-b64String.length % 4 + 4) % 4)
    }

    private fun String.hexStringToByteArray(): ByteArray =
        chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}


// Penjelasan:
// Enkripsi dan Dekripsi: Kita menggunakan Cipher dengan mode AES/CBC/PKCS5Padding untuk enkripsi dan dekripsi.
// Derivasi Kunci dan IV: Kita menggunakan MessageDigest untuk menghasilkan MD5 dan mendapatkan kunci dan IV.
// Padding: Kita menggunakan PKCS5Padding yang secara otomatis ditangani oleh Cipher di Kotlin.
// Konversi Hexadecimal: Fungsi toHexString dan hexStringToByteArray digunakan untuk konversi antara byte array dan string hexadecimal.
// Base64 Padding: Fungsi addBase64Padding menambahkan padding ke string Base64 jika diperlukan.
// Pastikan untuk menambahkan dependensi JSON di build.gradle jika Anda belum melakukannya:
// dependencies {
//    implementation "org.json:json:20210307"
// }