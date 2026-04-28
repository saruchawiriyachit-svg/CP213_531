package com.example.lablearnandroid.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// 1. สร้าง ViewModel ที่จัดการ Event ข้อผิดพลาด
class ErrorViewModel : ViewModel() {
    
    // การใช้ Channel เหมาะสมกับ One-time event อย่าง Snackbar มากกว่า State ทั่วไป
    // เพราะเวลาเกิดการหมุนจอ (Recomposition) หรือ State อื่นอัปเดต มันจะไม่ถูกโชว์ซ้ำ
    private val _errorChannel = Channel<String>()
    
    // แปลง Channel ให้กลายเป็น Flow เพื่อให้ Compose คอยดักฟัง (Observe)
    val errorFlow = _errorChannel.receiveAsFlow()

    // ฟังก์ชันจำลองการเกิดปัญหาระหว่างทำงาน
    fun triggerError() {
        viewModelScope.launch {
            // ส่งข้อความไปในท่อ Channel
            _errorChannel.send("เกิดข้อผิดพลาดในการเชื่อมต่อ! รหัส: ERR-${(100..999).random()}")
        }
    }
}

// 2. สร้างหน้า UI พร้อม Scaffold
@Composable
fun SnackbarErrorScreen(viewModel: ErrorViewModel = viewModel()) {
    
    // ใช้งาน SnackbarHostState เพื่อเป็นตัวสั่งการและแสดง Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // 3. ใช้ LaunchedEffect ดักฟัง Error จาก Flow (นี่คือกลไกของ Side Effect)
    LaunchedEffect(key1 = Unit) { // Unit เพื่อให้ LaunchedEffect เริ่มครอบคลุมตลอดอายุของ Composable นี้
        
        // .collect เป็น Suspend Function ที่จะแขวนการทำงานไปเรื่อยๆ เพื่อรอรับข้อมูลตัวต่อไป
        viewModel.errorFlow.collect { errorMessage ->
            
            // สั่งโชว์ Snackbar ซึ่งการ .showSnackbar ก็เป็น Suspend function เช่นกัน
            // แปลว่าถ้ามีการเรียกยิง Error รัวๆ มันจะค่อยๆ แสดงทีละอันจนจบไป 
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "ปิด"
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { 
            // ผูก snackbarHostState ที่เราสร้าง เข้าไปใน Layout โครงสร้างของ Scaffold
            SnackbarHost(hostState = snackbarHostState) 
        }
    ) { innerPadding ->
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // 4. ปุ่มสำหรับเรียกใช้งานเพื่อทดสอบ
            Button(onClick = { viewModel.triggerError() }) {
                Text("Trigger Error")
            }
        }
    }
}
