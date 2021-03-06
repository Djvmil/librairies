package com.suntelecoms.authenticate.util

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
object Utils {
    // utility function
    private fun bytesToHexString(bytes: ByteArray): String {
        // http://stackoverflow.com/questions/332079
        val sb = StringBuffer()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    // generate a hash
    @JvmStatic
    fun sha256(s: String): String {
        val digest: MessageDigest
        val hash: String
        return try {
            digest = MessageDigest.getInstance("SHA-256")
            digest.update(s.toByteArray())
            hash = bytesToHexString(digest.digest())
            hash
        } catch (e1: NoSuchAlgorithmException) {
            s
        }
    }

    @JvmStatic
    fun encrypt(input: String): String {
        // TODO not a real encrypt but make the job for the moment
        return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
    }

    @JvmStatic
    fun decrypt(input: String): String {
        return String(Base64.decode(input, Base64.DEFAULT))
    }
}