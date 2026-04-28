package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 10: App Widget Concept
class Part10Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold { innerPadding ->
                    AppWidgetConceptScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppWidgetConceptScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        Text("App Widget (Study Conceptual)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "การทำ App Widget ไม่สามารถทำได้ภายใน Activity เดียว เพราะ Widget ถูกคุมด้วย AppWidgetProvider ของ Android Home Screen (Launcher) \n\n" +
                   "CONCEPT:\n" +
                   "1. ในอดีต: ต้องเขียน XML RemoteViews และเชื่อมโยงผ่าน BroadcastReceiver \n" +
                   "2. ปัจจุบัน: พัฒนาด้วย Jetpack Glance (Compose for Widgets) ซึ่งอนุญาตให้เราเขียนโค้ดสไตล์ Compose ควบคุม Widget บนจอ Home Screen ได้\n" +
                   "3. แต่ตัว Glance เองไม่ใช่ Jetpack Compose ปกติ (เพราะรันบน Launcher) จึงใช้ Widget กล่องข้อความ และ Column เฉพาะของตัว Glance เอง\n\n" +
                   "*หากต้องการทดสอบ Widget จริง จำเป็นต้องใส่ library androidx.glance:glance-appwidget และประกาศเข้า AndroidManifest.xml แบบระบุ meta-data ครับ",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
