package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 12: Modal Bottom Sheet & Middle Dialog
class Part12Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold { innerPadding ->
                    InteractiveOverlaysScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveOverlaysScreen(modifier: Modifier = Modifier) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showBottomSheet = true }) {
                Text("Open Modal Bottom Sheet")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDialog = true }) {
                Text("Open Middle Dialog")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "CONCEPT:\n" +
                "1. Modal Bottom Sheet (ป๊อปอัปสไลด์จากด้านล่าง) เหมาะสำหรับเครื่องมือหรือตัวเลือกการตั้งค่าที่ไม่ถึงกับเป็นหน้าต่างสำคัญ\n" +
                "2. Middle Dialog (AlertDialog) เหมาะสำหรับข้อความที่ต้องการดึงจุดสนใจจากผู้ใช้อย่างชัดเจน เช่น การเตือน การยืนยันลบของ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp)) {
                Text("นี่คือ Bottom Sheet!", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Text("มันจะลอยขึ้นมาจากด้านล่าง และปิดง่ายๆ ด้วยการเลื่อนลง (ปัด) หรือคลิกด้านนอกกล่อง")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("แน่ใจหรือไม่?") },
            text = { Text("ลบข้อมูลทั้งหมดและดำเนินการต่อหรือไม่? การกระทำนี้ไม่สามารถย้อนกลับได้ (นี่คือตัวอย่าง Middle Dialog)") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("ใช่, ลบเลย") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("ยกเลิก") }
            }
        )
    }
}
