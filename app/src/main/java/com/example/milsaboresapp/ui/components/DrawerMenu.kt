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
    onNavigateToRegistro: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(240.dp)
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
        Spacer(Modifier.height(12.dp))

        // 2. Línea divisoria para separar la opción inferior
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 3. Opción de "Registrarse" (Parte inferior)
        TextButton(onClick = onNavigateToRegistro) {
            Text("Registrarse", color = ColorText)
        }
        TextButton(onClick = onNavigateToLogin) {
            Text("Iniciar Sesion", color = ColorText)
        }
}
    }

