package com.example.project.feature.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ─── Pastel palette ───────────────────────────────────────────────────────────
private val CreamWhite   = Color(0xFFFFF9F0)
private val SoftPeach    = Color(0xFFFFD6B8)
private val PastelPurple = Color(0xFFCBA6F7)
private val LilacMist    = Color(0xFFE8D5FF)
private val BabyBlue     = Color(0xFFB8DEFF)
private val SkyBlue      = Color(0xFFD6EEFF)
private val SoftPink     = Color(0xFFFFBFD6)
private val WarmYellow   = Color(0xFFFFF3B0)
private val TextDark     = Color(0xFF4A3F6B)
private val TextMid      = Color(0xFF7A6A9B)

// ─── Floating bubble data ────────────────────────────────────────────────────
private data class Bubble(
    val emoji: String,
    val x: Float,        // as fraction 0..1
    val y: Float,
    val size: Float,
    val speed: Int,      // animation millis
    val amplitude: Float // how much it floats up/down
)

private val bubbles = listOf(
    Bubble("🌸", 0.08f, 0.10f, 34f, 2400, 12f),
    Bubble("⭐", 0.85f, 0.08f, 28f, 2800, 10f),
    Bubble("🌙", 0.15f, 0.82f, 30f, 3000, 14f),
    Bubble("💛", 0.80f, 0.78f, 26f, 2600, 11f),
    Bubble("🌟", 0.50f, 0.06f, 24f, 2200, 9f),
    Bubble("🍭", 0.93f, 0.45f, 32f, 3200, 13f),
    Bubble("✨", 0.05f, 0.50f, 22f, 2000, 8f),
    Bubble("🎀", 0.45f, 0.90f, 28f, 2700, 10f),
)

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {

    // ── Entry animation states ────────────────────────────────────────────────
    var startAnim by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        startAnim = true
        delay(2800)        // show for 2.8 s, then navigate
        onSplashComplete()
    }

    // Jelly avatar bounce
    val infiniteTransition = rememberInfiniteTransition(label = "jelly_bounce")
    val jellyY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "jellyY"
    )

    // Sparkle rotation
    val sparkleRot by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ), label = "sparkleRot"
    )

    // Pulsing glow behind Jelly
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.92f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    // Title alpha + scale
    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(900, delayMillis = 400), label = "titleAlpha"
    )
    val titleScale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.7f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "titleScale"
    )

    // Subtitle alpha
    val subtitleAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(800, delayMillis = 900), label = "subAlpha"
    )

    // Jelly avatar scale (pop in)
    val jellyScale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "jellyScale"
    )

    // Dots alpha (loading indicator)
    val dotsAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(600, delayMillis = 1400), label = "dotsAlpha"
    )

    // ── Full-screen gradient background ──────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(LilacMist, CreamWhite, SkyBlue),
                    center = Offset(0.5f, 0.35f),
                    radius = 1800f
                )
            )
    ) {
        // ── Floating background blobs ─────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-60).dp, y = (-40).dp)
                .clip(CircleShape)
                .background(PastelPurple.copy(alpha = 0.25f))
                .blur(40.dp)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 60.dp)
                .clip(CircleShape)
                .background(SoftPink.copy(alpha = 0.30f))
                .blur(40.dp)
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 40.dp)
                .clip(CircleShape)
                .background(BabyBlue.copy(alpha = 0.28f))
                .blur(35.dp)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = 60.dp)
                .clip(CircleShape)
                .background(WarmYellow.copy(alpha = 0.35f))
                .blur(30.dp)
        )

        // ── Floating emoji bubbles ────────────────────────────────────────────
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val localDensity = LocalDensity.current
            val screenWDp = with(localDensity) { constraints.maxWidth.toDp() }
            val screenHDp = with(localDensity) { constraints.maxHeight.toDp() }

            bubbles.forEachIndexed { index, bubble ->
                val bubbleTransition = rememberInfiniteTransition(label = "bubble_$index")
                val floatY by bubbleTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -bubble.amplitude,
                    animationSpec = infiniteRepeatable(
                        animation = tween(bubble.speed, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "floatY_$index"
                )

                val bubbleAlpha by animateFloatAsState(
                    targetValue = if (startAnim) 0.85f else 0f,
                    animationSpec = tween(600, delayMillis = 200 + index * 120),
                    label = "bubbleAlpha_$index"
                )

                Box(
                    modifier = Modifier
                        .offset(
                            x = screenWDp * bubble.x - (bubble.size / 2).dp,
                            y = screenHDp * bubble.y - (bubble.size / 2).dp + floatY.dp
                        )
                        .alpha(bubbleAlpha)
                ) {
                    Text(text = bubble.emoji, fontSize = bubble.size.sp)
                }
            }
        }


        // ── Main content ──────────────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Glow ring ────────────────────────────────────────────────────
            Box(contentAlignment = Alignment.Center) {
                // Outer soft glow
                Box(
                    modifier = Modifier
                        .size(175.dp)
                        .scale(glowScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    PastelPurple.copy(alpha = 0.35f),
                                    SoftPeach.copy(alpha = 0.20f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Jelly avatar circle
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(jellyScale)
                        .offset(y = jellyY.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(WarmYellow, SoftPeach, SoftPink)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🐶", fontSize = 68.sp)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── "Welcome to" label ───────────────────────────────────────────
            Text(
                text = "Welcome to",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextMid,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .alpha(titleAlpha)
                    .scale(titleScale)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── "Decidr" brand title ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .alpha(titleAlpha)
                    .scale(titleScale),
                contentAlignment = Alignment.Center
            ) {
                // Soft background pill behind the title
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    PastelPurple.copy(alpha = 0.18f),
                                    SoftPink.copy(alpha = 0.18f)
                                )
                            )
                        )
                )
                Text(
                    text = "Decidr",
                    fontSize = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    letterSpacing = (-1).sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Tagline ───────────────────────────────────────────────────────
            Text(
                text = "let's decide together 🌟",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextMid,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(subtitleAlpha)
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── Bouncing dots loader ──────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(dotsAlpha)
            ) {
                listOf(0, 1, 2).forEach { dotIndex ->
                    val dotTransition = rememberInfiniteTransition(label = "dot_$dotIndex")
                    val dotY by dotTransition.animateFloat(
                        initialValue = 0f, targetValue = -8f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(450, delayMillis = dotIndex * 140, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ), label = "dotY_$dotIndex"
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .offset(y = dotY.dp)
                            .clip(CircleShape)
                            .background(
                                when (dotIndex) {
                                    0 -> PastelPurple
                                    1 -> SoftPink
                                    else -> BabyBlue
                                }
                            )
                    )
                }
            }
        }
    }
}
