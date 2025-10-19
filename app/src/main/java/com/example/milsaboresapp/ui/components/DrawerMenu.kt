package com.example.milsaboresapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.ui.theme.ColorBackground
import com.example.milsaboresapp.ui.theme.ColorText

@Composable
fun DrawerMenu(
    onNavigateToHome: () -> Unit,
    onNavigateToProductos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(ColorBackground)
            .padding(top = 48.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Menú principal",
            style = MaterialTheme.typography.titleMedium,
            color = ColorText
        )

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = onNavigateToHome) {
            Text("Inicio", color = ColorText)
        }

        TextButton(onClick = onNavigateToProductos) {
            Text("Productos", color = ColorText)
        }
    }
}
