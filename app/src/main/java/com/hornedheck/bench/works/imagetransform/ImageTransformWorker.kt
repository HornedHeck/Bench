package com.hornedheck.bench.works.imagetransform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.hornedheck.bench.works.Worker
import com.hornedheck.bench.works.WorkerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


class ImageTransformWorker : Worker() {

    override val batchSize: Int = 4
    private val scales = List(batchSize) {
        1.0 / (batchSize + 1) * (it + 1)
    }


    override fun run(context: Context, i: Int) {
        resizeBitmapMultithreaded(
            loadImage(context, getImageFilename(i)),
            8,
            scales[i]
        )
    }

    private fun loadImage(context: Context, path: String): Bitmap {
        return context.assets.open(path).use {
            BitmapFactory.decodeStream(it)
        }
    }

    private fun getImageFilename( iterationNum: Int): String {
        val imgNumber = (iterationNum + 1) % 4
        return "images/$imgNumber.heic"
    }

    private fun resizeBitmapMultithreaded(
        src: Bitmap,
        numThreads: Int,
        scale: Double,
    ): Bitmap {

        val targetWidth = (src.width * scale).toInt()
        val targetHeight = (src.width * scale).toInt()

        val dest = Bitmap.createBitmap(targetWidth, targetHeight, src.config)

        val chunkHeight = targetHeight / numThreads

        runBlocking {
            List(numThreads) { i ->
                async(Dispatchers.Default) {
                    val start = chunkHeight * i
                    val end = start + chunkHeight
                    for (y in start until end) {
                        for (x in 0 until targetWidth) {
                            val oldX = x * src.width / targetWidth
                            val oldY = y * src.height / targetHeight
                            dest.setPixel(x, y, src.getPixel(oldX, oldY))
                        }
                    }
                }
            }.awaitAll()
        }

        return dest
    }

}