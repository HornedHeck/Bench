package com.hornedheck.bench

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hornedheck.bench.ui.theme.BenchTheme
import com.hornedheck.bench.works.imagetransform.ImageTransformWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "TIME"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BenchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }


        CoroutineScope(Dispatchers.Default).launch {
            val imageWorker = ImageTransformWorker()
            Log.v(TAG, "Starting ImageTransformWorker.")
            val imageResult = imageWorker.run(this@MainActivity)
            Log.v(TAG, StringBuilder().append(
                "ImageTransformWorker finished:\n",
                "Batches: ${imageResult.batchCount}\n",
                "Batch size: ${imageResult.batchSize}\n",
                "Total time: ${imageResult.totalTimeMs} ms\n",
                "Per batch times:\n",
                imageResult.timesMs.mapIndexed { b, v ->
                    "\tBatch $b: $v ms"
                }.joinToString(separator = "\n") { it }
            ).toString())
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BenchTheme {
        Greeting("Android")
    }
}