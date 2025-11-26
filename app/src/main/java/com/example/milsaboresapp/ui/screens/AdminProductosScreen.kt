package com.example.milsaboresapp.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosAdminScreen(
    viewModel: ProductViewModel,
    onAgregarClick: () -> Unit,
    onEditarClick: (Producto) -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Administrar Productos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // --- FILTRO DE CATEGORÍA ---
            if (categorias.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = categoriaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("todos") },
                            onClick = {
                                expanded = false
                                viewModel.setCategoria("todos")
                            }
                        )

                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = {
                                    expanded = false
                                    viewModel.setCategoria(categoria)
                                }
                            )
                        }
                    }
                }
            }

            // --- LISTA DE PRODUCTOS ---
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, fontSize = 18.sp)
                                Text("SKU: ${producto.sku}")
                                Text("Precio: $${producto.precio}")
                                if (producto.enOferta) {
                                    Text("Oferta: $${producto.precioEnOferta}", color = Color.Red)
                                }
                                Text("Stock: ${producto.stock}")
                            }

                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(
                                    onClick = { onEditarClick(producto) }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(
                                    onClick = {
                                        // Aquí deberás usar repository.deleteProducto(producto.id)
                                        // según cómo tengas implementado el repositorio
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
