package com.example.lablearnandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

// Mission 6: View Interoperability (WebView จาก XML)
class WebViewModelPart6 : ViewModel() {
    var currentUrl by mutableStateOf("https://www.google.com")
        private set

    fun updateUrl(newUrl: String) {
        val formattedUrl = if (!newUrl.startsWith("http://") && !newUrl.startsWith("https://")) {
            "https://$newUrl"
        } else {
            newUrl
        }
        currentUrl = formattedUrl
    }
}

class Part6Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroidTheme {
                Scaffold { innerPadding ->
                    WebViewScreenPart6(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WebViewScreenPart6(modifier: Modifier = Modifier, viewModel: WebViewModelPart6 = viewModel()) {
    var inputText by remember { mutableStateOf(viewModel.currentUrl) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = { viewModel.updateUrl(inputText) }),
                label = { Text("Enter Web URL") }
            )
            Button(onClick = { viewModel.updateUrl(inputText) }) {
                Text("Go")
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // แบบดึงมาจาก XML ตามโจทย์เป๊ะ 100%
                val view = LayoutInflater.from(context).inflate(R.layout.webview_layout, null, false)
                val webView = view.findViewById<WebView>(R.id.myWebView)
                
                webView.apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient() 
                }
                view // Return wrapper
            },
            update = { view ->
                val webView = view.findViewById<WebView>(R.id.myWebView)
                webView.loadUrl(viewModel.currentUrl)
            }
        )
    }
}
