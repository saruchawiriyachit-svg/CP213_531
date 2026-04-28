package com.example.project.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Pastel color palette for Jelly the Mascot
private val PastelYellow = Color(0xFFFFF9C4)
private val PastelBlue = Color(0xFFE3F2FD)
private val PastelPink = Color(0xFFFCE4EC)
private val PastelGreen = Color(0xFFE8F5E9)
private val TextDarkGray = Color(0xFF424242)

/**
 * Mascot Avatar Component. 
 * Represents Jelly the Golden Retriever.
 */
@Composable
fun JellyAvatar(modifier: Modifier = Modifier, backgroundColor: Color = PastelYellow) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for an actual image asset of a Golden Retriever
        Text(text = "🐶", fontSize = 32.sp)
    }
}

/**
 * Conversational bubble featuring Jelly.
 * Used for Result screens or interactive prompts ("I think this option looks good!").
 */
@Composable
fun JellyMessageBubble(
    message: String,
    modifier: Modifier = Modifier,
    isJellySpeaking: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isJellySpeaking) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isJellySpeaking) {
            JellyAvatar(
                modifier = Modifier.padding(end = 12.dp),
                backgroundColor = PastelYellow
            )
        }
        
        Surface(
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isJellySpeaking) 4.dp else 20.dp,
                bottomEnd = if (isJellySpeaking) 20.dp else 4.dp
            ),
            color = if (isJellySpeaking) PastelBlue else MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = if (isJellySpeaking) TextDarkGray else MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * A cute loading view displaying Jelly thinking.
 * Default message: "Let's decide together 🐶"
 */
@Composable
fun JellyLoadingView(
    modifier: Modifier = Modifier,
    message: String = "Let's decide together 🐶"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bouncing")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.offset(y = offsetY.dp)) {
            JellyAvatar(modifier = Modifier.size(80.dp), backgroundColor = PastelPink)
        }
        Spacer(modifier = Modifier.height(32.dp))
        
        CircularProgressIndicator(
            color = PastelPink,
            trackColor = PastelBlue,
            strokeWidth = 3.dp,
            modifier = Modifier.size(36.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Minimalist empty state when there are no options or a decision couldn't be made.
 * Default message: "Hmm... let's think again"
 */
@Composable
fun JellyEmptyState(
    modifier: Modifier = Modifier,
    message: String = "Hmm... let's think again",
    actionContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        JellyAvatar(modifier = Modifier.size(100.dp), backgroundColor = PastelGreen)
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PastelGreen.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }

        if (actionContent != null) {
            Spacer(modifier = Modifier.height(32.dp))
            actionContent()
        }
    }
}
