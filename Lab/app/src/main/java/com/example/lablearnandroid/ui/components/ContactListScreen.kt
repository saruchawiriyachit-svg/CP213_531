package com.example.lablearnandroid.ui.components

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 1. ViewModel (Mock ข้อมูลและ State isLoading)
class ContactListViewModel : ViewModel() {

    // State ถือ List ของรายชื่อผู้ติดต่อ
    private val _contacts = mutableStateListOf<String>()
    val contacts: List<String> = _contacts

    // State จัดการหน้า UI ระหว่างโหลด
    var isLoading by mutableStateOf(false)
        private set

    // ใช้จำลอง Pagination (หน้าถัดไปคือการดึงตัวอักษรถัดไป)
    private var currentPage = 0
    private val alphabet = ('A'..'Z').toList()

    init {
        // เริ่มต้นโหลดข้อมูลทันทีที่ถูกเรียกใช้
        loadMoreContacts()
    }

    // ฟังก์ชันถูก Trigger เพื่อโหลดข้อมูลเพิ่ม
    fun loadMoreContacts() {
        // ถ้ายุ่งอยู่ หรือโหลดครบ A-Z แล้วก็ไม่ต้องทำอะไร
        if (isLoading || currentPage >= alphabet.size) return

        viewModelScope.launch {
            isLoading = true
            
            // 3. จำลองการโหลดข้อมูลเพิ่ม (หน่วงเวลา 2 วินาที)
            delay(2000)

            // สมมติว่าหน้าหนึ่งโหลดทีละ 3 ตัวอักษร
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

// 2. Composable Screen สำหรับแสดงหน้า UI
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(viewModel: ContactListViewModel = viewModel()) {
    val contacts = viewModel.contacts
    val listState = rememberLazyListState()

    // เช็คว่าผู้ใช้เลื่อน Scroll มาถึงไอเท็มอันสุดท้ายของลิสต์หรือยัง
    val isScrolledToEnd by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            
            lastVisibleItem.index >= totalItems - 1
        }
    }

    // 3. Trigger ฟังก์ชันเมื่อถูกเลื่อนมาจนสุดทางเพื่อจำลองการ Pagination
    LaunchedEffect(isScrolledToEnd) {
        if (isScrolledToEnd && !viewModel.isLoading) {
            viewModel.loadMoreContacts()
        }
    }

    // จับข้อมูลมากลุ่มตามตัวขวาแรก (Grouping) เช่น A, B, C...
    val groupedContacts = contacts.groupBy { it.split(" ")[1] } // ดึงตัวอักษรจากแพทเทิร์น "Contact A - X"

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        groupedContacts.forEach { (initial, contactsInGroup) ->
            
            // 2. LazyColumn รองรับ Sticky Header
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

            // แสดงรายการลิสต์ของตัวอักษรนั้นๆ
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

        // 4. แสดง CircularProgressIndicator ที่ด้านล่างสุดระหว่างที่กำลังโหลดชุดถัดไป
        if (viewModel.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
