@file:OptIn(ExperimentalBenchmarkConfigApi::class)

package com.hornedheck.bench

import androidx.benchmark.ExperimentalBenchmarkConfigApi
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.MemoryUsageMetric.Mode.Max
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.hornedheck.bench.works.encyption.EncryptDecryptWorker
import com.hornedheck.bench.works.imagetransform.ImageTransformWorker
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun testBench() {
        val worker = EncryptDecryptWorker()
        benchmarkRule.measureRepeated(
            "com.hornedheck.bench", metrics = listOf(
                PowerMetric(PowerMetric.Type.Power()),
                MemoryUsageMetric(Max)
            ), iterations = 10
        ) {
            worker.run(context)
        }
    }

}