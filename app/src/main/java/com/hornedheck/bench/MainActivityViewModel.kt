package com.hornedheck.bench

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hornedheck.bench.works.State
import com.hornedheck.bench.works.db.DBWorker
import com.hornedheck.bench.works.encyption.EncryptDecryptWorker
import com.hornedheck.bench.works.imagetransform.ImageTransformWorker
import com.hornedheck.bench.works.mapping.MapperWorker
import com.hornedheck.bench.works.remoteapi.RemoteApiWorker
import com.hornedheck.common.BenchmarkType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(context: Application) : AndroidViewModel(context) {

    private val _state = MutableStateFlow<State>(State.Ready)
    val state: StateFlow<State> = _state

    fun startBenchmarks(benchmarkType: BenchmarkType, context: Context, iteration: Int) {
        viewModelScope.launch(
            Dispatchers.Default
        ) {
            val worker = getWorker(benchmarkType)
            _state.value = State.Progress(worker::class.simpleName ?: "UnknownWorker")
            val result = worker.run(context, false, iteration)
            _state.value = State.Results(
                runResult = """
                    ${worker::class.simpleName}
                    Batch size: ${result.batchSize}
                    Total time (ms): ${result.time}
                """.trimIndent()
            )
        }

    }

    private fun getWorker(type: BenchmarkType) = when (type) {
        BenchmarkType.IMAGE_TRANSFORM -> ImageTransformWorker()
        BenchmarkType.ENCRYPTION -> EncryptDecryptWorker()
        BenchmarkType.MAPPER -> MapperWorker()
        BenchmarkType.REMOTE_API -> RemoteApiWorker()
        BenchmarkType.DB -> DBWorker(1 * 10 * 1000L, getApplication())
    }
}