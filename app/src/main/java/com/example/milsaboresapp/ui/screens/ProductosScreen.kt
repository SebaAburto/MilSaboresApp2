package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.ui.components.ProductCard // Importar el componente de la tarjeta
import com.example.milsaboresapp.ui.theme.*
import com.example.milsaboresapp.viewmodel.ProductViewModel // Importar el ViewModel desde su nueva ubicación

@Composable
fun ProductosScreen(
    // Inyectamos el ViewModel
    viewModel: ProductViewModel = viewModel()
) {
    // Observamos los estados de los datos del ViewModel
    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorLight)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // CABECERA Y LOGO
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text("Nuestros Productos", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(10.dp))

        //LazyRow la lista horizontal de chips deslizables (categorias)
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoria == categoriaSeleccionada,
                    onClick = { viewModel.setCategoria(categoria) }, // Al hacer clic, se filtra
                    label = { Text(categoria.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }) }, // Capitaliza
                    leadingIcon = if (categoria == categoriaSeleccionada) {
                        { Icon(Icons.Filled.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                    } else null
                )
            }
        }

        // 3. LISTA DE PRODUCTOS
        if (productos.isEmpty() && categorias.isNotEmpty() && categoriaSeleccionada != "todos") {
            // Mensaje si no hay productos en la categoría seleccionada
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay productos en la categoría: ${categoriaSeleccionada.uppercase()}",
                    style = MaterialTheme.typography.titleMedium)
            }
        } else if (productos.isEmpty()) {
            // Indicador de carga inicial
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Muestra la lista desplazable de productos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp), // Padding a los lados
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(productos) { producto ->
                    // Utilizamos el ProductCard para cada elemento
                    ProductCard(producto = producto)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductosScreenPreview() {
    // NOTA: Para que el Preview funcione con el ViewModel, necesitarías un ViewMdel Mock o
    // usar un PreviewParameterProvider en un escenario real, pero lo dejamos simple aquí.
    ProductosScreen(viewModel = viewModel())
}