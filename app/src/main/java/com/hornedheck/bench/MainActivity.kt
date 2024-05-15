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
import com.hornedheck.bench.works.encyption.EncryptDecryptWorker
import com.hornedheck.bench.works.imagetransform.ImageTransformWorker
import com.hornedheck.bench.works.inflate.InflateWorker
import com.hornedheck.bench.works.remoteapi.RemoteApiWorker
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
            val worker = RemoteApiWorker()
            Log.v(TAG, "Starting ${worker.javaClass.name.substringAfterLast('.')}.")
            val result = worker.run(this@MainActivity)
            Log.v(TAG, StringBuilder().append(
                "${worker::class.simpleName} finished:\n",
                "Batches: ${result.batchCount}\n",
                "Batch size: ${result.batchSize}\n",
                "Total time: ${result.totalTimeMs} ms\n",
                "Per batch times:\n",
                result.timesMs.mapIndexed { b, v ->
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