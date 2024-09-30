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
    val batchSize: Int,
    val time : Long
)

abstract class Worker {

    protected abstract val batchSize: Int

    protected open fun prepare(context: Context){}

    fun run(context: Context): WorkerResult {
        prepare(context)
//        val batteryManager = context.getSystemService(BatteryManager::class.java)
//
//        val batteryStart = batteryManager.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER)
        var totalTimeMs = 0L
        repeat(batchSize) { i ->
            val time = measureTimeMillis {
                run(context, i)
            }
            Trace.setCounter(EXECUTION_TIME_TAG, time)
            totalTimeMs += time
        }
//        val batteryEnd = batteryManager.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER)
//        Trace.setCounter(BATTERY_START, batteryStart)
//        Trace.setCounter(BATTERY_END, batteryEnd)
//        Trace.setCounter(BATTERY_DELTA, batteryStart - batteryEnd)

        return WorkerResult(
            batchSize,
            totalTimeMs
        )
    }

    protected abstract fun run(context: Context, i: Int)
}