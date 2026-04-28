package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 4: Advanced Gestures & Interactive UI (Swipe to Dismiss To-Do List)
class TodoViewModelPart4 : ViewModel() {
    private val _todoList = mutableStateListOf(
        "Buy groceries for dinner",
        "Finish Android lab assignment",
        "Call Mom and Dad",
        "Clean the bedroom",
        "Read a new book"
    )
    val todoList: List<String> = _todoList

    fun removeItem(item: String) {
        _todoList.remove(item)
    }
}

class Part4Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoListScreenPart4(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreenPart4(modifier: Modifier = Modifier, viewModel: TodoViewModelPart4 = viewModel()) {
    val items = viewModel.todoList

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = items,
            key = { it } 
        ) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { swipeValue ->
                    if (swipeValue == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.removeItem(item)
                        return@rememberSwipeToDismissBoxState true
                    }
                    return@rememberSwipeToDismissBoxState false
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false, 
                backgroundContent = {
                    val color by animateColorAsState(
                        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent,
                        label = "Color"
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(color).padding(end = 24.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon", tint = Color.White)
                        }
                    }
                }
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(20.dp)) {
                    Text(text = item, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
