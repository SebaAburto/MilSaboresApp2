package com.example.milsaboresapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.milsaboresapp.ui.screens.HomeScreen
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //funcion de punto de entrada a la app
        setContent {
            //se aplica el tema
            MilSaboresAppTheme {
                // Surface define el fondo sobre el cual se dibuja la UI
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a tu pantalla principal (HomeScreen)
                    HomeScreen()
                }
            }
        }
    }
}
