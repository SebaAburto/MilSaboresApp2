package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.di.AppGraph
import com.example.milsaboresapp.ui.components.ContenidoDetalle
import com.example.milsaboresapp.ui.theme.ColorBackground
import com.example.milsaboresapp.ui.theme.ColorPrimary
import com.example.milsaboresapp.ui.theme.ColorText
import com.example.milsaboresapp.viewmodel.ProductoDetalleViewModel
import com.example.milsaboresapp.viewmodel.ProductoDetalleViewModelFactory
import androidx.compose.foundation.layout.WindowInsets

@Composable
fun provideProductoDetalleViewModel(sku: String): ProductoDetalleViewModel {
    return viewModel(
        factory = ProductoDetalleViewModelFactory(
            sku = sku,
            productosRepo = AppGraph.productosRepo,
            carritoRepo = AppGraph.cartRepo
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreen(navController: NavController, productoSku: String) {

    val viewModel = provideProductoDetalleViewModel(productoSku)
    val uiState by viewModel.uiState.collectAsState()
    val cantidad by viewModel.cantidad.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Producto",
                        style = MaterialTheme.typography.titleLarge,
                        color = ColorText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = ColorText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPrimary
                ),
                windowInsets = WindowInsets(0.dp)
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
        // WindowsInset se usa para ajustar el contenido de scaffold para elementos inexistentes (ej: unbottombar)

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ColorBackground)
                .verticalScroll(rememberScrollState())
        ) {


            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null || uiState.producto == null -> {
                    Text(
                        text = uiState.error ?: "¡Lo sentimos! Producto no encontrado.",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {

                    ContenidoDetalle(
                        producto = uiState.producto!!,
                        cantidad = cantidad,
                        onIncrementar = viewModel::incrementarCantidad,
                        onDecrementar = viewModel::decrementarCantidad,
                        onAgregarCarrito = viewModel::agregarACarrito
                    )
                }
            }
        }
    }
}