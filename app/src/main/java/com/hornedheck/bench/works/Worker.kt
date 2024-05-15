package com.hornedheck.bench.works

import android.content.Context
import kotlin.system.measureTimeMillis

data class WorkerResult(
    val batchCount: Int,
    val batchSize: Int,
    val totalTimeMs: Long,
    val timesMs: List<Long>
)

abstract class Worker {

    protected abstract val batchCount: Int
    protected abstract val batchSize: Int

    fun run(context: Context): WorkerResult {
        val times = List(batchCount) { batchNum ->
            measureTimeMillis {
                repeat(batchSize) { i ->
                    run(context, batchNum, i)
                }
            }
        }
        return WorkerResult(
            batchCount,
            batchSize,
            totalTimeMs = times.sum(),
            timesMs = times
        )
    }

    protected abstract fun run(context: Context, batchNum: Int, i: Int)

}