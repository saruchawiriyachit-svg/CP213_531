package com.example.lablearnandroid.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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

@Composable
fun AnimatedDonutChart(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier.size(200.dp),
    strokeWidth: Dp = 40.dp
) {
    // 3. Animation: สถานะความคืบหน้าของ Animation (0.0 ถึง 1.0) สำหรับกางองศาจาก 0 ถึง 360
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(key1 = values) {
        // เริ่มรัน Animation เมื่อ Component ถูกเริ่มโหลด
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val total = values.sum()
            val proportions = values.map { it * 360f / total }
            
            // เริ่มต้นวาดที่มุม -90 องศาคือตำแหน่ง 12 นาฬิกา (Top)
            var startAngle = -90f 

            val strokeWidthPx = strokeWidth.toPx()
            
            // ปรับระยะการวาดโดยหักลบความหนาของเส้นออกไปครึ่งหนึ่ง (เพื่อไม่ให้เส้นล้นขอบ Canvas)
            val drawingSize = Size(
                width = size.width - strokeWidthPx,
                height = size.height - strokeWidthPx
            )
            val topLeftOffset = Offset(strokeWidthPx / 2f, strokeWidthPx / 2f)

            // จำกัดองศาทั้งหมดของกราฟตามรอบ Animation เฟรมปัจจุบัน
            val currentMaxSweepAngle = 360f * animationProgress.value

            for (i in proportions.indices) {
                val targetSweepAngle = proportions[i]
                val previousSweepTotal = proportions.take(i).sum()

                // คำนวณ Sweep Angle ของชิ้นนี้ให้สอดคล้องกับค่า Animation
                val animatedSweepAngle = if (previousSweepTotal + targetSweepAngle <= currentMaxSweepAngle) {
                    targetSweepAngle // อนิเมชันผ่านชิ้นนี้มาหมดแล้ว โชว์เต็มองศา
                } else if (previousSweepTotal < currentMaxSweepAngle) {
                    currentMaxSweepAngle - previousSweepTotal // อนิเมชันอยู่ในระหว่างวาดชิ้นนี้
                } else {
                    0f // อนิเมชันยังมาไม่ถึง วาดยาว 0
                }

                if (animatedSweepAngle > 0f) {
                    // 2. ใช้วิธีการวาด drawArc และตั้งค่า useCenter = false ร่วมกับ Stroke
                    drawArc(
                        color = colors.getOrNull(i) ?: Color.Gray,
                        startAngle = startAngle,
                        sweepAngle = animatedSweepAngle,
                        useCenter = false, // ไม่เติมสีเข้าไปตรงกลาง (เว้นกลาง)
                        topLeft = topLeftOffset,
                        size = drawingSize,
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt) // ทำให้ตรงกลางใส โผล่แต่ขอบ
                    )
                }

                startAngle += targetSweepAngle
            }
        }
    }
}
