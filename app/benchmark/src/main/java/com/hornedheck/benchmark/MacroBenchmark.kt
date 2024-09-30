package com.hornedheck.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiSelector
import com.hornedheck.benchmark.metrics.CounterMetric
import com.hornedheck.common.BENCHMARK_TYPE_KEY
import com.hornedheck.common.BenchmarkType
import com.hornedheck.common.EXECUTION_TIME_KEY
import com.hornedheck.common.EXECUTION_TIME_TAG
import com.hornedheck.common.ITERATIONS_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
class MacroBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun fixedIterations() = benchmarkRule.measureRepeated(
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
        startActivityAndWait {
            it.putExtra(BENCHMARK_TYPE_KEY, BenchmarkType.IMAGE_TRANSFORM.name)
        }
        this.device.findObject(UiSelector().text("Finished")).waitForExists(300000)
    }

    @Test
    fun fixedTime() = benchmarkRule.measureRepeated(
        packageName = "com.hornedheck.bench",
        metrics = listOf(
            MemoryUsageMetric(MemoryUsageMetric.Mode.Last),
            CounterMetric(EXECUTION_TIME_TAG),
            CounterMetric(ITERATIONS_TAG),
            StartupTimingMetric()
//            CounterMetric(BATTERY_START),
//            CounterMetric(BATTERY_END),
//            CounterMetric(BATTERY_DELTA),
//            PowerMetric(PowerMetric.Type.Battery())
        ),
        iterations = 10,
        startupMode = StartupMode.WARM,
        compilationMode = CompilationMode.Full()
    ) {
        pressHome()
        startActivityAndWait {
            it.putExtra(BENCHMARK_TYPE_KEY, BenchmarkType.DB.name)
            it.putExtra(EXECUTION_TIME_KEY, 1 * 60 * 1000L)
        }
        this.device.findObject(UiSelector().text("Finished")).waitForExists(300000)
    }

}