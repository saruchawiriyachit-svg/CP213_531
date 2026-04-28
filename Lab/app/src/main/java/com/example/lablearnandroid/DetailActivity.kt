package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 1. รับค่า String extra ที่แนบมาจาก Intent
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "No message received"

        setContent {
            LabLearnAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Hello from Detail Activity!", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Sent info: $message", color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // 3. ปุ่ม 'Close'
                            Button(onClick = { 
                                finish() // สั่งปิด DetailActivity กลับไปก่อนหน้า
                            }) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }

    // เมื่อ Activity ถูกทำงานเข้าโหมดปิดหน้าจอ จะทำการ Override Animation
    @Suppress("DEPRECATION")
    override fun finish() {
        super.finish()
        // สั่งให้ DetailActivity ไหลกลับลงล่าง (Slide out down) ส่วน MainActivity เดิมอยู่นิ่งๆ (stay)
        // หมายเหตุ: สำหรับ API > 34 วิธีล่าสุดคือการใช้ overrideActivityTransition() แต่ตัวนี้ครอบคลุมสุด 
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }
}
