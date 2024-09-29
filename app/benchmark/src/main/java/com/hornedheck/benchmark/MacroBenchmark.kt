package com.hornedheck.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MemoryCountersMetric
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceMetric
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.UiSelector
import com.hornedheck.benchmark.metrics.CounterMetric
import com.hornedheck.common.BATTERY_DELTA
import com.hornedheck.common.BATTERY_END
import com.hornedheck.common.BATTERY_START
import com.hornedheck.common.BENCHMARK_TYPE_KEY
import com.hornedheck.common.BenchmarkType
import com.hornedheck.common.EXECUTION_TIME_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
class MacroBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.hornedheck.bench",
        metrics = listOf(
            MemoryUsageMetric(MemoryUsageMetric.Mode.Last),
            CounterMetric(EXECUTION_TIME_TAG),
            StartupTimingMetric()
//            CounterMetric(BATTERY_START),
//            CounterMetric(BATTERY_END),
//            CounterMetric(BATTERY_DELTA),
//            PowerMetric(PowerMetric.Type.Battery())
        ),
        iterations = 10,
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.Partial()
    ) {
        pressHome()
        startActivityAndWait{
            it.putExtra(BENCHMARK_TYPE_KEY, BenchmarkType.IMAGE_TRANSFORM.name)
        }
        this.device.findObject(UiSelector().text("Finished")).waitForExists(300000)
    }
}