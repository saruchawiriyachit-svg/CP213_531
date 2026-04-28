package com.example.lablearnandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.lablearnandroid.ui.theme.LabLearnAndroidTheme

class SensorActivity : ComponentActivity() {

    private val viewModel: SensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                viewModel.startLocationUpdates()
            }
        }

        setContent {
            LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                        requestPermission = {
                            requestPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startAccelerometer()
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopAccelerometer()
        viewModel.stopLocationUpdates()
    }
}

@Composable
fun SensorScreen(
    viewModel: SensorViewModel,
    modifier: Modifier = Modifier,
    requestPermission: () -> Unit
) {
    val accelerometerData by viewModel.accelerometerData.collectAsState()
    val locationData by viewModel.locationData.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Sensors (MVVM Architecture)", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = "Accelerometer:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "X: ${accelerometerData.first}")
        Text(text = "Y: ${accelerometerData.second}")
        Text(text = "Z: ${accelerometerData.third}")

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Location:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (locationData != null) {
            Text(text = "Latitude: ${locationData?.first}")
            Text(text = "Longitude: ${locationData?.second}")
        } else {
            Text(text = "Location not available or no permission")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { requestPermission() }) {
                Text("Request Location Permission")
            }
        }
    }
}
