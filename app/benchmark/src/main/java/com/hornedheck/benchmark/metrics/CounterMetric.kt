@file:OptIn(ExperimentalMetricApi::class, ExperimentalPerfettoTraceProcessorApi::class)

package com.hornedheck.benchmark.metrics

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceMetric
import androidx.benchmark.perfetto.ExperimentalPerfettoTraceProcessorApi
import androidx.benchmark.perfetto.PerfettoTraceProcessor

open class CounterMetric(
    private val displayName: String,
    private val counterName: String,
    val resultConverter: (List<Double>) -> MeasurementResult = MeasurementResult::Multiple
) : TraceMetric() {

    override fun getMeasurements(
        captureInfo: CaptureInfo,
        traceSession: PerfettoTraceProcessor.Session
    ): List<Measurement> {
        val counterValues = traceSession.query(
            """
            SELECT track.name, value
            FROM counter
                LEFT JOIN process_counter_track as track on counter.track_id = track.id
            WHERE track.name LIKE '$counterName'
            """.trimIndent()
        )

        val values = counterValues.map {
            it.double("value")
        }.toList().ifEmpty {
            listOf(0.0)
        }

        return when (val result = resultConverter(values)) {
            is MeasurementResult.Single -> {
                listOf(Measurement(displayName, result.result))
            }

            is MeasurementResult.Multiple -> {
                listOf(Measurement(displayName, result.result))
            }
        }
    }
}

sealed interface MeasurementResult {
    data class Single(val result: Double) : MeasurementResult
    data class Multiple(val result: List<Double>) : MeasurementResult
}

class CounterSumMetric(name: String) : CounterMetric(
    name,
    name,
    resultConverter = { MeasurementResult.Single(it.sum()) }
)
