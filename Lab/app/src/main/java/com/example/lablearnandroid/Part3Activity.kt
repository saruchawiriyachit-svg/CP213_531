package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 3: กราฟวงกลมแบบโดนัทด้วย Canvas
class Part3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedDonutChartPart3(
                            values = listOf(30f, 40f, 30f),
                            colors = listOf(Color.Red, Color.Green, Color.Blue),
                            modifier = Modifier.size(250.dp),
                            strokeWidth = 35.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedDonutChartPart3(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 40.dp
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(key1 = values) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val total = values.sum()
            val proportions = values.map { it * 360f / total }
            var startAngle = -90f 

            val strokeWidthPx = strokeWidth.toPx()
            val drawingSize = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)
            val topLeftOffset = Offset(strokeWidthPx / 2f, strokeWidthPx / 2f)
            val currentMaxSweepAngle = 360f * animationProgress.value

            for (i in proportions.indices) {
                val targetSweepAngle = proportions[i]
                val previousSweepTotal = proportions.take(i).sum()
                val animatedSweepAngle = if (previousSweepTotal + targetSweepAngle <= currentMaxSweepAngle) {
                    targetSweepAngle
                } else if (previousSweepTotal < currentMaxSweepAngle) {
                    currentMaxSweepAngle - previousSweepTotal
                } else {
                    0f
                }

                if (animatedSweepAngle > 0f) {
                    drawArc(
                        color = colors.getOrNull(i) ?: Color.Gray,
                        startAngle = startAngle,
                        sweepAngle = animatedSweepAngle,
                        useCenter = false, // แบบจำกัดโดนัท (ใสตรงกลาง)
                        topLeft = topLeftOffset,
                        size = drawingSize,
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt)
                    )
                }
                startAngle += targetSweepAngle
            }
        }
    }
}
