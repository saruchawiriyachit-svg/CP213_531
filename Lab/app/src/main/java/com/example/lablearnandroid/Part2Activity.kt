package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Mission 2: ViewModel สำรับ Load ข้อมูลทีละน้อย (Pagination)
class ContactListViewModel : ViewModel() {
    private val _contacts = mutableStateListOf<String>()
    val contacts: List<String> = _contacts

    var isLoading by mutableStateOf(false)
        private set

    private var currentPage = 0
    private val alphabet = ('A'..'Z').toList()

    init {
        loadMoreContacts()
    }

    fun loadMoreContacts() {
        if (isLoading || currentPage >= alphabet.size) return
        viewModelScope.launch {
            isLoading = true
            delay(2000) // หน่วงเวลาจำลอง 2 วินาที
            val lettersToLoad = alphabet.drop(currentPage * 3).take(3)
            val newContacts = lettersToLoad.flatMap { letter ->
                (1..5).map { index -> "Contact $letter - $index" }
            }
            _contacts.addAll(newContacts)
            currentPage++
            isLoading = false
        }
    }
}

class Part2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactListScreenPart2(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreenPart2(modifier: Modifier = Modifier, viewModel: ContactListViewModel = viewModel()) {
    val contacts = viewModel.contacts
    val listState = rememberLazyListState()

    val isScrolledToEnd by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= totalItems - 1
        }
    }

    LaunchedEffect(isScrolledToEnd) {
        if (isScrolledToEnd && !viewModel.isLoading) {
            viewModel.loadMoreContacts() // โหลดหน้าถัดไปเมื่อกวาดลงสุด
        }
    }

    val groupedContacts = contacts.groupBy { it.split(" ")[1] } 

    LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
        groupedContacts.forEach { (initial, contactsInGroup) ->
            // Sticky Header แบ่งอักษรตัวแรก
            stickyHeader {
                Text(
                    text = initial,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(contactsInGroup) { contactName ->
                Text(
                    text = contactName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
        if (viewModel.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
