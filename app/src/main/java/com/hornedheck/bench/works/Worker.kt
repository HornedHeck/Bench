package com.hornedheck.bench.works

import android.content.Context
import android.os.BatteryManager
import android.os.BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER
import android.os.Trace
import com.hornedheck.common.BATTERY_DELTA
import com.hornedheck.common.BATTERY_END
import com.hornedheck.common.BATTERY_START
import com.hornedheck.common.EXECUTION_TIME_TAG
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
        val batteryManager = context.getSystemService(BatteryManager::class.java)

        val times = List(batchCount) { batchNum ->
            val batteryStart = batteryManager.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER)
            var totalTimeMs = 0L
            repeat(batchSize) { i ->
                val time = measureTimeMillis {
                    run(context, batchNum, i)
                }
                Trace.setCounter(EXECUTION_TIME_TAG, time)
                totalTimeMs += time
            }
            val batteryEnd = batteryManager.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER)
            Trace.setCounter(BATTERY_START, batteryStart)
            Trace.setCounter(BATTERY_END, batteryEnd)
            Trace.setCounter(BATTERY_DELTA, batteryStart - batteryEnd)
            totalTimeMs
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