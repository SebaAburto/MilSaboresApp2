package com.example.milsaboresapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.milsaboresapp.navigation.NavigationWrapper
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MilSaboresAppTheme {
                NavigationWrapper()
            }
        }
    }
}
