package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 8: Adaptive Layouts (Responsive Profile)
class Part8Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold { innerPadding ->
                    AdaptiveProfileScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AdaptiveProfileScreen(modifier: Modifier = Modifier) {
    // ใช้ BoxWithConstraints เพื่อเข้าถึง maxWidth / maxHeight ในบริเวณหน้าจอที่ถูกวาด
    BoxWithConstraints(modifier = modifier.fillMaxSize().padding(16.dp)) {
        
        // กฎ: ถ้าน้อยกว่า 600dp แสดงเป็นหน้าจอมือถือ (Column) ถ้ามากกว่า แสดงเป็นแนวแท็บเล็ต/แนวนอน (Row)
        if (maxWidth < 600.dp) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(size = 150)
                Spacer(modifier = Modifier.height(24.dp))
                ProfileInfo()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(size = 200)
                Spacer(modifier = Modifier.width(32.dp))
                // ให้ฝั่ง Text ขยายเต็มพื้นที่ที่เหลือ
                Box(modifier = Modifier.weight(1f)) {
                    ProfileInfo()
                }
            }
        }
    }
}

@Composable
fun ProfileImage(size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Profile", color = Color.DarkGray)
    }
}

@Composable
fun ProfileInfo() {
    Column {
        Text("Sarah Connor", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Software Developer & Tech Enthusiast. Building responsive UI on Jetpack Compose to fit any screen sizes.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
