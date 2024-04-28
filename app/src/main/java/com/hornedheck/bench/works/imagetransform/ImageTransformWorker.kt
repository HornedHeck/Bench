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

private const val BATCH_SIZE = 4
private const val BATCH_COUNT = 10
private val scales = List(BATCH_COUNT) {
    1.0 / (BATCH_COUNT + 1) * (it + 1)
}

class ImageTransformWorker : Worker {

    override fun run(context: Context): WorkerResult {
        val times = List(BATCH_COUNT) { batchNum ->
            measureTimeMillis {
                repeat(BATCH_SIZE) { i ->
                    resizeBitmapMultithreaded(
                        loadImage(context, getImageFilename(batchNum, i)),
                        8,
                        scales[i]
                    )
                }
            }
        }
        return WorkerResult(
            BATCH_COUNT,
            BATCH_SIZE,
            totalTimeMs = times.sum(),
            timesMs = times
        )
    }

    private fun loadImage(context: Context, path: String): Bitmap {
        return context.assets.open(path).use {
            BitmapFactory.decodeStream(it)
        }
    }

    private fun getImageFilename(batchNum: Int, iterationNum: Int): String {
        val imgNumber = (batchNum + 1) * (iterationNum + 1) % 4
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