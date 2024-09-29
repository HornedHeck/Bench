@file:OptIn(ExperimentalMetricApi::class, ExperimentalPerfettoTraceProcessorApi::class)

package com.hornedheck.benchmark.metrics

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceMetric
import androidx.benchmark.perfetto.ExperimentalPerfettoTraceProcessorApi
import androidx.benchmark.perfetto.PerfettoTraceProcessor

class CounterMetric(private val counterName: String) : TraceMetric() {

    override fun getResult(
        captureInfo: CaptureInfo,
        traceSession: PerfettoTraceProcessor.Session
    ): List<Measurement> {
        val counterValue = traceSession.query(
            """
            SELECT track.name, value
            FROM counter
                LEFT JOIN process_counter_track as track on counter.track_id = track.id
            WHERE track.name LIKE '$counterName'
            """.trimIndent()
        )

        val (name, values) = counterValue.map {
            it.string("name") to listOf(it.double("value"))
        }.reduce { a, b ->
            a.first to a.second + b.second
        }

        return listOf(Measurement(name, values))
    }
}