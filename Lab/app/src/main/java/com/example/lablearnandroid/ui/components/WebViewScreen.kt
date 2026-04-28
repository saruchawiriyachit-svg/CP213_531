package com.example.lablearnandroid.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// 1. ViewModel (มี State เก็บค่า URL String)
class WebViewModel : ViewModel() {
    
    // ตั้งค่าเริ่มต้นเป็น google.com
    var currentUrl by mutableStateOf("https://www.google.com")
        private set

    fun updateUrl(newUrl: String) {
        // รูปแบบสั้นๆ เติม https:// เล็กน้อยหากผู้ใช้ลืมใส่
        val formattedUrl = if (!newUrl.startsWith("http://") && !newUrl.startsWith("https://")) {
            "https://$newUrl"
        } else {
            newUrl
        }
        currentUrl = formattedUrl
    }
}

@Composable
fun WebViewScreen(viewModel: WebViewModel = viewModel()) {
    // State สำหรับรับค่าที่ผู้ใช้กำลังพิมพ์แบบ Real-time บนคีย์บอร์ด
    var inputText by remember { mutableStateOf(viewModel.currentUrl) }

    Column(modifier = Modifier.fillMaxSize()) {
        
        // 4. แถบควบคุม TextField + ปุ่ม "Go" วางแนวนอน
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = { viewModel.updateUrl(inputText) }
                ),
                label = { Text("Enter Web URL") }
            )

            Button(onClick = { viewModel.updateUrl(inputText) }) {
                Text("Go")
            }
        }

        // 2. เรียกใช้ AndroidView เพื่อโหลด View ดั้งเดิมบน Jetpack Compose
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // โค้ดในส่วน factory จะถูกใช้แค่ ครั้งแรกครั้งเดียว! (ตอนสร้างตัว View)
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    
                    // 3. ป้องกันไม่ให้แอปเด้งออกไปเปิด Chrome ภายนอก หากไม่ใส่แอปจะพฤติกรรมแปลกๆ
                    webViewClient = WebViewClient() 
                }
            },
            update = { webView ->
                // โค้ดในส่วน update จะถูกทำงานตลอดทุกครั้งที่ State ที่มันสังเกตเห็น (viewModel.currentUrl) เปลี่ยนค่า
                // คอยเป็นตัวสั่งให้ WebView เก่าที่ถูกวาดค้างอยู่ ทำการรีโหลดตามลิงก์ใหม่!
                webView.loadUrl(viewModel.currentUrl)
            }
        )
    }
}
