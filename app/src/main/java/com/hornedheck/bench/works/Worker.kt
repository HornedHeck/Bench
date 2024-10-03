package com.hornedheck.bench.works

import android.content.Context
import android.os.BatteryManager
import android.os.BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER
import android.os.Trace
import com.hornedheck.common.BATTERY_DELTA
import com.hornedheck.common.BATTERY_END
import com.hornedheck.common.BATTERY_START
import com.hornedheck.common.EXECUTION_TIME_TAG
import com.hornedheck.common.STEP_TIME_TAG
import kotlin.system.measureTimeMillis

data class WorkerResult(
    val batchSize: Int,
    val time: Long
)

abstract class Worker {

    protected abstract val batchSize: Int

    protected open fun prepare(context: Context) {}

    fun run(context: Context, isBatteryEnabled: Boolean, iteration: Int): WorkerResult {
        prepare(context)

        var batteryManager: BatteryManager? = null
        var batteryStart: Long = 0
        if (isBatteryEnabled) {
            batteryManager = context.getSystemService(BatteryManager::class.java)

            batteryStart = batteryManager.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER)
        }
        var totalTimeMs = 0L
        repeat(batchSize) { i ->
            val time = measureTimeMillis {
                run(context, i, iteration)
            }
            Trace.setCounter(EXECUTION_TIME_TAG, time)
            totalTimeMs += time
        }
        if (isBatteryEnabled) {
            val batteryEnd = batteryManager!!.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER)
            Trace.setCounter(BATTERY_START, batteryStart)
            Trace.setCounter(BATTERY_END, batteryEnd)
            Trace.setCounter(BATTERY_DELTA, batteryStart - batteryEnd)
        }

        return WorkerResult(
            batchSize,
            totalTimeMs
        )
    }

    protected abstract fun run(context: Context, batchIteration: Int, iteration: Int)
}