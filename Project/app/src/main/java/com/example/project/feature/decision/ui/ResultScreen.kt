package com.example.project.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R
import com.example.project.feature.decision.domain.Option
import com.example.project.feature.decision.domain.ProsCons
import com.example.project.feature.decision.domain.Recommendation
import com.example.project.ui.components.JellyMessageBubble

// ── Soft palette used only in this screen ────────────────────────────────────
private val ProGreen      = Color(0xFF4CAF50)
private val ProGreenSoft  = Color(0xFFE8F5E9)
private val ConRedSoft    = Color(0xFFFFEBEE)
private val WinnerGold    = Color(0xFFFFF3B0)
private val WinnerBorder  = Color(0xFFFFD54F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onBack: () -> Unit,
    recommendation: Recommendation? = Recommendation(
        recommendedOptionId = "opt-1",
        reasoning = "Noodles offer the best balance of delicious taste and affordability! The price factor weighs heavily here, and ramen wins by a clear margin. Plus, it's super comforting on a long day — exactly the kind of energy boost you need! 🍜",
        confidenceScore = 0.85f,
        prosAndCons = mapOf(
            "opt-1" to ProsCons(score = 92, pros = listOf("Budget-friendly", "Very tasty", "Quick to get"), cons = listOf("Might be hot today")),
            "opt-2" to ProsCons(score = 78, pros = listOf("Incredible taste", "Healthy option"), cons = listOf("A bit expensive", "Takes longer"))
        )
    ),
    options: List<Option> = listOf(
        Option("opt-1", "Noodles", "Yummy bowl of ramen"),
        Option("opt-2", "Sushi", "Fresh salmon rolls")
    ),
    onFeedback: (Boolean) -> Unit = {},
    onNavigateToChat: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var feedbackState by remember { mutableStateOf<Boolean?>(null) }
    var showThanks by remember { mutableStateOf(false) }

    // ── Loading / error state ─────────────────────────────────────────────────
    if (recommendation == null || options.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.jelly_thinking))
            Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
                Text(stringResource(R.string.go_back))
            }
        }
        return
    }

    val bestOption  = options.find { it.id == recommendation.recommendedOptionId } ?: options.first()
    val confPercent = (recommendation.confidenceScore * 100).toInt()
    val confLabel   = when {
        confPercent >= 85 -> "Very confident"
        confPercent >= 70 -> "Fairly confident"
        confPercent >= 55 -> "Somewhat confident"
        else              -> "Leaning towards"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.decision_result)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            // ── 1. Jelly greeting (contextual) ────────────────────────────────
            JellyMessageBubble(
                message = "I've thought about it carefully... and my pick is ${bestOption.title}! 🐾 Here's why:",
                isJellySpeaking = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── 2. Winner card ────────────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(WinnerGold, MaterialTheme.colorScheme.primaryContainer)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Winner",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "🏆  Jelly's Pick",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = bestOption.title,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                        if (bestOption.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = bestOption.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 3. Confidence score ───────────────────────────────────────────
            SectionLabel("Jelly's Confidence")
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = confLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$confPercent%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    var progress by remember { mutableFloatStateOf(0f) }
                    LaunchedEffect(recommendation.confidenceScore) { progress = recommendation.confidenceScore }
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        label = "confidence_anim",
                        animationSpec = tween(1400)
                    )
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 4. Why this option? (full reasoning) ─────────────────────────
            SectionLabel("Why ${bestOption.title}?")
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🐶", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp, top = 2.dp))
                    Text(
                        text = recommendation.reasoning,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 5. Score comparison bar chart ─────────────────────────────────
            SectionLabel("Score Comparison")
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    options.forEach { option ->
                        val optionData     = recommendation.prosAndCons[option.id]
                        val score          = optionData?.score ?: 0
                        val targetProgress = score.toFloat() / 100f
                        val isWinner       = option.id == recommendation.recommendedOptionId
                        val barColor       = if (isWinner) MaterialTheme.colorScheme.primary
                                             else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.65f)

                        var currentProgress by remember { mutableFloatStateOf(0f) }
                        LaunchedEffect(targetProgress) { currentProgress = targetProgress }
                        val animatedProgress by animateFloatAsState(
                            targetValue = currentProgress,
                            animationSpec = tween(durationMillis = 1100, delayMillis = 200),
                            label = "chart_anim_${option.id}"
                        )

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isWinner) {
                                        Text("🏆 ", fontSize = 14.sp)
                                    }
                                    Text(
                                        text = option.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                Text(
                                    text = "$score / 100",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = barColor
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(9.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedProgress)
                                        .fillMaxHeight()
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = if (isWinner)
                                                    listOf(barColor, barColor.copy(alpha = 0.75f))
                                                else
                                                    listOf(barColor, barColor)
                                            )
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 6. Per-option Pros & Cons breakdown ───────────────────────────
            SectionLabel(stringResource(R.string.option_breakdown))
            Spacer(modifier = Modifier.height(10.dp))

            recommendation.prosAndCons.forEach { (optionId, prosCons) ->
                val option   = options.find { it.id == optionId } ?: return@forEach
                val isWinner = optionId == recommendation.recommendedOptionId

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isWinner)
                            WinnerGold.copy(alpha = 0.35f)
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Option title row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isWinner) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🏆", fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = option.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            // Score badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isWinner) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${prosCons.score}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (isWinner) MaterialTheme.colorScheme.onPrimary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (option.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = option.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 0.5.dp)

                        // Pros
                        if (prosCons.pros.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ProGreenSoft)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "✅  Pros",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = ProGreen
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            prosCons.pros.forEach { pro ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("• ", color = ProGreen, fontWeight = FontWeight.Bold)
                                    Text(pro, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Cons
                        if (prosCons.cons.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ConRedSoft)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "⚠️  Cons",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            prosCons.cons.forEach { con ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("• ", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                    Text(con, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 7. Summary takeaway ───────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📝  Bottom Line",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val winnerScore   = recommendation.prosAndCons[bestOption.id]?.score ?: 0
                    val runnerUp      = options.filter { it.id != bestOption.id }
                        .maxByOrNull { recommendation.prosAndCons[it.id]?.score ?: 0 }
                    val runnerScore   = runnerUp?.let { recommendation.prosAndCons[it.id]?.score } ?: 0
                    val diff          = winnerScore - runnerScore

                    val summaryText = buildString {
                        append("${bestOption.title} scored $winnerScore/100")
                        if (runnerUp != null) {
                            append(", beating ${runnerUp.title} ($runnerScore) by $diff points.")
                        } else {
                            append(".")
                        }
                        append(" With $confPercent% confidence, Jelly thinks this is a clear winner for you. 🐾")
                    }

                    Text(
                        text = summaryText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── 8. Feedback ───────────────────────────────────────────────────
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.was_this_helpful),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { feedbackState = true; showThanks = true; onFeedback(true) },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = if (feedbackState == true) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = if (feedbackState == true) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = "Like",
                                tint = if (feedbackState == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        IconButton(
                            onClick = { feedbackState = false; showThanks = true; onFeedback(false) },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = if (feedbackState == false) MaterialTheme.colorScheme.error else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = if (feedbackState == false) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                                contentDescription = "Dislike",
                                tint = if (feedbackState == false) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    if (showThanks) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.thanks_feedback),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── 9. Chat with Jelly ────────────────────────────────────────────
            Button(
                onClick = onNavigateToChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "💬  ${stringResource(R.string.chat_with_jelly)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Reusable section header ───────────────────────────────────────────────────
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}