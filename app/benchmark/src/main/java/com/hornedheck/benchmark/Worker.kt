package com.hornedheck.benchmark

import android.content.Context
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.system.measureTimeMillis

class EncryptDecryptWorker {

    private val key: SecretKey = KeyGenerator.getInstance("AES")
        .let {
            it.init(256)
            it.generateKey()
        }

    val batchCount = 1
    val batchSize = 10

    fun run(context: Context) {
        repeat(batchSize){
            run(context , batchNum = 0, i = it)
        }
    }

    fun run(context: Context, batchNum: Int, i: Int) {
        val src = getSrc(context)
        val (encrypted, iv) = encrypt(src)
        decrypt(encrypted, iv)
    }

    private fun getSrc(context: Context) = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nec tincidunt praesent semper feugiat nibh sed pulvinar proin gravida. Rutrum tellus pellentesque eu tincidunt tortor aliquam. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt. Tellus cras adipiscing enim eu turpis. Tellus id interdum velit laoreet. Enim ut tellus elementum sagittis vitae et leo duis. Tellus in metus vulputate eu scelerisque felis imperdiet. Enim facilisis gravida neque convallis a. Faucibus interdum posuere lorem ipsum dolor sit. A condimentum vitae sapien pellentesque habitant morbi tristique. Pellentesque habitant morbi tristique senectus. Tincidunt id aliquet risus feugiat in ante metus dictum at.

        Velit scelerisque in dictum non consectetur a. Sollicitudin aliquam ultrices sagittis orci a scelerisque. Est sit amet facilisis magna etiam tempor orci eu lobortis. Fermentum iaculis eu non diam phasellus vestibulum lorem sed. Pulvinar sapien et ligula ullamcorper malesuada proin libero nunc. Eros donec ac odio tempor orci dapibus ultrices in. Non sodales neque sodales ut etiam sit amet nisl. Condimentum mattis pellentesque id nibh tortor. Nibh nisl condimentum id venenatis a condimentum. Auctor neque vitae tempus quam. Vel fringilla est ullamcorper eget nulla facilisi. Id interdum velit laoreet id donec ultrices tincidunt. Aliquam malesuada bibendum arcu vitae elementum curabitur vitae nunc sed. In nulla posuere sollicitudin aliquam ultrices sagittis. Aliquet sagittis id consectetur purus ut faucibus. Leo duis ut diam quam nulla porttitor massa. Pulvinar proin gravida hendrerit lectus a. Et netus et malesuada fames ac. Mi in nulla posuere sollicitudin.

        Facilisi morbi tempus iaculis urna. Tempor commodo ullamcorper a lacus vestibulum sed. Sit amet nisl purus in mollis nunc. In cursus turpis massa tincidunt dui. Amet mauris commodo quis imperdiet massa tincidunt. Dignissim sodales ut eu sem integer vitae justo eget. Scelerisque eu ultrices vitae auctor eu. Fames ac turpis egestas integer eget aliquet. Tempus egestas sed sed risus pretium quam vulputate dignissim suspendisse. Facilisis gravida neque convallis a cras semper auctor. Porta nibh venenatis cras sed felis eget velit aliquet. Ac tincidunt vitae semper quis lectus.

        Phasellus faucibus scelerisque eleifend donec pretium vulputate. Non tellus orci ac auctor augue mauris augue. Diam in arcu cursus euismod. Leo vel fringilla est ullamcorper eget. Nisl vel pretium lectus quam id leo in. Quisque non tellus orci ac auctor augue mauris augue. Nisl vel pretium lectus quam id leo in vitae turpis. Nisi est sit amet facilisis magna etiam tempor orci. Scelerisque viverra mauris in aliquam. Commodo quis imperdiet massa tincidunt nunc pulvinar. Sem integer vitae justo eget.

        Diam maecenas ultricies mi eget mauris pharetra et ultrices. Porta non pulvinar neque laoreet. Egestas fringilla phasellus faucibus scelerisque eleifend. Lorem ipsum dolor sit amet consectetur adipiscing elit. Quis ipsum suspendisse ultrices gravida dictum fusce ut placerat. Ornare suspendisse sed nisi lacus sed viverra tellus. Donec enim diam vulputate ut. In hendrerit gravida rutrum quisque. Netus et malesuada fames ac turpis egestas sed. Tincidunt tortor aliquam nulla facilisi cras fermentum odio. Arcu odio ut sem nulla pharetra. Feugiat sed lectus vestibulum mattis. Sit amet massa vitae tortor condimentum lacinia quis vel eros. Velit scelerisque in dictum non consectetur a erat. Ut sem nulla pharetra diam sit amet nisl. Habitant morbi tristique senectus et netus. Tellus id interdum velit laoreet id donec. Faucibus pulvinar elementum integer enim neque volutpat ac tincidunt.
    """.trimIndent()


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