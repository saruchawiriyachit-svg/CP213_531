package com.example.lablearnandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 7: Multi-Activity Transitions (ศูนย์รวมเมนูและทรานซิชัน)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold { innerPadding ->
                    MenuScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class MenuOption(val title: String, val clazz: Class<out ComponentActivity>, val transitionType: TransitionType)
enum class TransitionType(val description: String) { SLIDE_UP("Slide Up"), FADE("Fade In"), SLIDE_LEFT("Slide In Left"), DEFAULT("Default") }

@Composable
fun MenuScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val menuItems = listOf(
        MenuOption("Part 1: Compose Animation & Motion", Part1Activity::class.java, TransitionType.SLIDE_UP),
        MenuOption("Part 2: Complex Lists & Pagination", Part2Activity::class.java, TransitionType.FADE),
        MenuOption("Part 3: Graphics, Effects & Canvas", Part3Activity::class.java, TransitionType.SLIDE_LEFT),
        MenuOption("Part 4: Advanced Gestures (Swipe to Dismiss)", Part4Activity::class.java, TransitionType.DEFAULT),
        MenuOption("Part 5: Compose Side Effects", Part5Activity::class.java, TransitionType.SLIDE_UP),
        MenuOption("Part 6: View Interoperability (WebView)", Part6Activity::class.java, TransitionType.FADE),
        MenuOption("Part 8: Adaptive Layouts", Part8Activity::class.java, TransitionType.SLIDE_LEFT),
        MenuOption("Part 9: Collapsing Toolbar", Part9Activity::class.java, TransitionType.DEFAULT),
        MenuOption("Part 10: App Widget", Part10Activity::class.java, TransitionType.SLIDE_UP),
        MenuOption("Part 11: Skeleton Loading", Part11Activity::class.java, TransitionType.FADE),
        MenuOption("Part 12: Bottom Sheet & Middle Dialog", Part12Activity::class.java, TransitionType.SLIDE_LEFT)
    )

    LazyColumn(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item {
            Text("Android Advanced Course", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(vertical = 24.dp))
        }
        
        items(menuItems) { option ->
            Button(
                onClick = {
                    val intent = Intent(context, option.clazz)
                    // Mission 7: แสดงการใช้งาน Custom Transitions รันตาม Type 
                    val optionsBundle = when(option.transitionType) {
                        TransitionType.SLIDE_UP -> ActivityOptionsCompat.makeCustomAnimation(context, R.anim.slide_in_up, R.anim.stay).toBundle()
                        TransitionType.FADE -> ActivityOptionsCompat.makeCustomAnimation(context, R.anim.fade_in, R.anim.stay).toBundle()
                        TransitionType.SLIDE_LEFT -> ActivityOptionsCompat.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.stay).toBundle()
                        TransitionType.DEFAULT -> null // ใช้ default OS animation
                    }
                    context.startActivity(intent, optionsBundle)
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Text("${option.title} [${option.transitionType.description}]")
            }
        }
    }
}