package com.hornedheck.bench.works

import android.content.Context
import android.os.Trace
import com.hornedheck.common.ITERATIONS_TAG
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

abstract class FixedTimeWorker(private val timeToRun: Long) : Worker() {

    protected open val context : CoroutineContext = Dispatchers.Default

    override val batchSize: Int
        get() = 1

    protected abstract suspend fun runBatch(context: Context)

    final override fun run(context: Context, i: Int) {
        var counter = 0L
        val task = CoroutineScope(this.context + CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                throw throwable
            }
        }).launch(
            start = CoroutineStart.LAZY
        ) {
            while (true) {
                runBatch(context)
                counter += 1
            }
        }
        runBlocking {
            task.start()
            delay(timeToRun)
            task.cancel()
            Trace.setCounter(ITERATIONS_TAG, counter)
        }
    }
}