package com.hornedheck.bench.works.imagetransform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.hornedheck.bench.works.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking


class ImageTransformWorker : Worker() {

    override val batchSize: Int = 10

    private fun getScale(iteration: Int): Double {
        return (iteration + 1).toDouble() / 10 + 0.5
    }

    override fun run(context: Context, batchIteration: Int, iteration: Int) {
        resizeBitmapMultithreaded(
            loadImage(context, getImageFilename(iteration)),
            8,
            getScale(iteration)
        )
    }

    private fun loadImage(context: Context, path: String): Bitmap {
        return context.assets.open(path).use {
            BitmapFactory.decodeStream(it)
        }
    }

    private fun getImageFilename(iterationNum: Int): String {
        return "images/0.heic"
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