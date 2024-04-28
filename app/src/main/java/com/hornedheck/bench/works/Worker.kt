package com.hornedheck.bench.works

import android.content.Context

data class WorkerResult(
    val batchCount: Int,
    val batchSize: Int,
    val totalTimeMs: Long,
    val timesMs: List<Long>
)

interface Worker {

    fun run(context: Context): WorkerResult

}