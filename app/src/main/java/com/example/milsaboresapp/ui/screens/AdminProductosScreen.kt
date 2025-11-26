package com.example.milsaboresapp.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.milsaboresapp.viewmodel.ProductViewModel

@Composable
fun AdminProductosScreen(viewModel: ProductViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hola mundo")
    }
}
