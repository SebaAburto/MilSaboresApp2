package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.di.AppGraph
import com.example.milsaboresapp.model.ItemCarrito
import com.example.milsaboresapp.viewmodel.CarritoViewModel
import com.example.milsaboresapp.viewmodel.CarritoUiState
import java.text.NumberFormat
import java.util.Locale

// Función para inyectar el ViewModel (usando el AppGraph)
@Composable
fun provideCarritoViewModel(): CarritoViewModel {
    return viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(AppGraph.cartRepo) as T
        }
    })
}

// ----------------------------------------------------------------------
// PANTALLA PRINCIPAL
// ----------------------------------------------------------------------
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = provideCarritoViewModel()
) {
    val uiState by viewModel.ui.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Tu Carrito (${uiState.count} artículos)",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Tu carrito está vacío. ¡Añade algo! 🛒", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.items, key = { it.sku }) { item ->
                    CarritoItemCard( // Ahora resuelta
                        item = item,
                        onIncrementar = { viewModel.incrementar(item.sku) },
                        onDecrementar = { viewModel.decrementar(item.sku) },
                        onEliminar = { viewModel.eliminar(item.sku) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CarritoResumen(uiState = uiState, onFinalizarCompra = {
                viewModel.limpiar()
            })
        }
    }
}

// ----------------------------------------------------------------------
// COMPONENTES AUXILIARES
// ----------------------------------------------------------------------

@Composable
fun CarritoResumen(
    uiState: CarritoUiState,
    onFinalizarCompra: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total a Pagar:", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = uiState.totalCLP,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onFinalizarCompra,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.count > 0
            ) {
                Text("Finalizar Compra")
            }
        }
    }
}

@Composable
fun CarritoItemCard(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Precio Unitario: ${formatter.format(item.precio)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Subtotal: ${formatter.format(item.precio * item.cantidad)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Control de Cantidad (Add/Remove)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onDecrementar) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Decrementar cantidad"
                    )
                }

                Text(
                    text = item.cantidad.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onIncrementar) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Incrementar cantidad"
                    )
                }
            }

            // Botón Eliminar
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar ítem",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}