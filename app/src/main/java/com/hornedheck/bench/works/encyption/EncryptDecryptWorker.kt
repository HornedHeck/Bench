package com.hornedheck.bench.works.encyption

import android.content.Context
import com.hornedheck.bench.works.Worker
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptDecryptWorker : Worker() {

    private val key: SecretKey = KeyGenerator.getInstance("AES")
        .let {
            it.init(256)
            it.generateKey()
        }

    override val batchSize = 5

    override fun run(context: Context, i: Int) {
        val src = getSrc(context)
        val (encrypted, iv) = encrypt(src)
        decrypt(encrypted, iv)
    }

    private fun getSrc(context: Context) = context.assets
        .open("encryption/lorem_ipsum.txt")
        .reader()
        .use {
            it.readText()
        }

    private fun encrypt(src: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(src.encodeToByteArray()) to cipher.iv
    }

    private fun decrypt(src: ByteArray, iv : ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.doFinal(src).decodeToString()
    }

}