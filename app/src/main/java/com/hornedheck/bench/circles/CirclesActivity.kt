package com.hornedheck.bench.circles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hornedheck.common.EXECUTION_TIME_KEY
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sin

class CirclesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val time = intent.getIntExtra(EXECUTION_TIME_KEY, 10 * 1000)

        setContent {
            Surface {
                Circles(time)
            }
        }
    }

    @Composable
    private fun Circles(time: Int) {

        var isAnimated by remember { mutableStateOf(true) }
        if (isAnimated) {

            LaunchedEffect(Unit) {
                delay(time.toLong())
                isAnimated = false
            }

            val transition = rememberInfiniteTransition(label = "UI Load")

            val animationPhase: Float by transition.animateFloat(
                initialValue = 0f,
                targetValue = 2f,
                label = "Scale phase",
                animationSpec = InfiniteRepeatableSpec(
                    animation = tween(time / 10, easing = LinearEasing)
                )
            )

            LazyVerticalGrid(GridCells.Fixed(5), horizontalArrangement = Arrangement.Center) {
                items(50) {
                    Canvas(
                        modifier = Modifier
                            .aspectRatio(1.0f)
                            .fillMaxWidth(0.2f)
                    ) {
                        drawCircle(
                            Color.LightGray,
                            radius = size.minDimension / 3.0f * abs(sin((animationPhase + (it % 5) / 5f) * Math.PI)).toFloat()
                        )
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Finished")
            }
        }

    }
}