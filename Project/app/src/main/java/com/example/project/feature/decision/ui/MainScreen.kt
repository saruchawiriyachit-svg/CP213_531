package com.example.project.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project.ui.components.JellyMessageBubble
import java.util.UUID
import com.example.project.feature.decision.domain.Decision
import com.example.project.feature.decision.domain.Option
import com.example.project.feature.decision.domain.Factor

data class OptionItem(val id: String = UUID.randomUUID().toString(), var value: String = "")
data class FactorItem(val id: String = UUID.randomUUID().toString(), var name: String = "", var weight: Float = 0.5f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToResult: (Decision) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf(OptionItem(), OptionItem())) }
    var factors by remember { mutableStateOf(listOf(FactorItem(name = "Price"), FactorItem(name = "Taste"))) }
    var isAdvancedMode by remember { mutableStateOf(false) }

    val validOptionsCount = options.count { it.value.isNotBlank() }
    val isValid = question.isNotBlank() && validOptionsCount >= 2

    val jellyMessage = when {
        question.isBlank() -> "Hi! What are we deciding today? 🐶"
        validOptionsCount < 2 -> "I need at least 2 options to help you decide!"
        !isAdvancedMode -> "Looks good! Ready when you are."
        else -> "Ooh, advanced mode! Let's fine-tune those factors."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top section with Jelly
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            JellyMessageBubble(
                message = jellyMessage,
                isJellySpeaking = true
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Question", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    placeholder = { Text("e.g. Where should we go for lunch?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text("Options", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(options) { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = option.value,
                        onValueChange = { newValue ->
                            options = options.toMutableList().also { it[index] = option.copy(value = newValue) }
                        },
                        placeholder = { Text("Option ${index + 1}") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    IconButton(
                        onClick = { options = options.filterIndexed { i, _ -> i != index } },
                        enabled = options.size > 2
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Option", tint = if (options.size > 2) MaterialTheme.colorScheme.error else Color.Gray)
                    }
                }
            }

            item {
                TextButton(
                    onClick = { options = options + OptionItem() },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Option")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Option")
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { isAdvancedMode = !isAdvancedMode }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Advanced Mode", fontWeight = FontWeight.Medium)
                    Switch(checked = isAdvancedMode, onCheckedChange = { isAdvancedMode = it })
                }
            }

            item {
                AnimatedVisibility(
                    visible = isAdvancedMode,
                    enter = expandVertically(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300))
                ) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text("Decision Factors", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        factors.forEachIndexed { index, factor ->
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = factor.name,
                                        onValueChange = { newValue ->
                                            factors = factors.toMutableList().also { it[index] = factor.copy(name = newValue) }
                                        },
                                        placeholder = { Text("Factor") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    IconButton(
                                        onClick = { factors = factors.filterIndexed { i, _ -> i != index } },
                                        enabled = factors.size > 1
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove Factor",
                                            tint = if (factors.size > 1) MaterialTheme.colorScheme.error else Color.Gray
                                        )
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Weight:", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                                    Slider(
                                        value = factor.weight,
                                        onValueChange = { newValue ->
                                            factors = factors.toMutableList().also { it[index] = factor.copy(weight = newValue) }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        TextButton(
                            onClick = { factors = factors + FactorItem() },
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Factor")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Factor")
                        }
                    }
                }
            }

            // ── Decide & History buttons — always at the bottom, always scrollable to ──
            item {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val decision = Decision(
                            id = UUID.randomUUID().toString(),
                            query = question.trim(),
                            options = options.filter { it.value.isNotBlank() }.map {
                                Option(id = it.id, title = it.value.trim(), description = "")
                            },
                            factors = factors.filter { it.name.isNotBlank() }.map {
                                Factor(id = it.id, name = it.name.trim(), weight = it.weight)
                            }
                        )
                        onNavigateToResult(decision)
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Decide for me!", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onNavigateToHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("View History")
                }
            }
        }
    }
}