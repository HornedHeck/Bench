package com.hornedheck.bench

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.hornedheck.bench.ui.theme.BenchTheme
import com.hornedheck.bench.works.State
import com.hornedheck.common.BENCHMARK_TYPE_KEY
import com.hornedheck.common.BenchmarkType
import com.hornedheck.common.ITERATION_KEY

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>(
        factoryProducer = { ViewModelProvider.AndroidViewModelFactory(application) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BenchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val state = viewModel.state.collectAsState().value) {
                        State.Ready -> ReadyState()
                        is State.Progress -> RunningState(name = state.workerName)
                        is State.Results -> ResultsState(runResults = state)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startBenchmarks(
            BenchmarkType.valueOf(
                intent.getStringExtra(BENCHMARK_TYPE_KEY) ?: BenchmarkType.IMAGE_TRANSFORM.name
            ),
            application,
            intent.getIntExtra(ITERATION_KEY, 0)
        )
    }

}

@Composable
fun ReadyState() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ready to Rock'n'roll")
    }
}

@Composable
fun RunningState(name: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(text = "Running $name")
    }
}

@Composable
fun ResultsState(runResults: State.Results) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Finished")
        Text(text = runResults.runResult)
    }
}