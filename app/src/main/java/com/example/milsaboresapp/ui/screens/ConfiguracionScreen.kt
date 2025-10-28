package com.example.milsaboresapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.viewmodel.ConfiguracionViewModel

@Composable
fun ConfiguracionScreen(viewModel: ConfiguracionViewModel = viewModel()) {

    val estadoCargado = viewModel.modoOscuroActivo.collectAsState()

    if (estadoCargado.value == null) {
        // Muestra un loader mientras DataStore carga el valor inicial
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val activo = estadoCargado.value!!

        // Animación de color: cambia suavemente entre oscuro y claro
        val colorFondo by animateColorAsState(
            targetValue = if (activo) Color(0xFF121212) else Color(0xFFFFF5E1),
            animationSpec = tween(600),
            label = "colorModoFondo"
        )
        val colorBoton by animateColorAsState(
            targetValue = if (activo) Color(0xFFBB86FC) else Color(0xFF6200EE),
            animationSpec = tween(600),
            label = "colorModoBoton"
        )
        val textoColor by animateColorAsState(
            targetValue = if (activo) Color(0xFFFFFFFF) else Color(0xFF000000),
            animationSpec = tween(600),
            label = "colorTexto"
        )

        Column(
            Modifier
                .fillMaxSize()
                .background(colorFondo) // Fondo animado
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Modo Oscuro Persistente",
                style = MaterialTheme.typography.headlineMedium,
                color = textoColor
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { viewModel.alternarModoOscuro() }, // Llama al ViewModel
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text(
                    if (activo) "Desactivar Modo Claro" else "Activar Modo Oscuro",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}