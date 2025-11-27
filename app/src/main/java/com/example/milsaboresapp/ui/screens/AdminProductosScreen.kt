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
fun AdminProductosScreen(
    //parametros necesarios para la funcionalidad de la screen
    viewModel: ProductViewModel,
    onAgregarClick: () -> Unit,
    onEditarClick: (Producto) -> Unit
) {
    // listas usadas con un State que actualice la screen cada vez que haya un cambio.
    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    // Estado que recuerda el producto a eliminar.
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    // DELETE
    if (productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Está seguro de que desea eliminar el producto: ${productoAEliminar!!.nombre} (SKU: ${productoAEliminar!!.sku})?") },
            confirmButton = {
                Button(
                    onClick = {
                        //función del ViewModel para eliminar.
                        viewModel.deleteProducto(productoAEliminar!!)
                        productoAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Administrar Productos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) { // C (Create)
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            //FILTRO DE CATEGORÍAS
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

            // --- LISTA DE PRODUCTOS (READ) ---
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productos, key = { it.sku }) { producto ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {

                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("SKU: ${producto.sku}", style = MaterialTheme.typography.bodySmall)
                                Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodySmall)
                                if (producto.enOferta) {
                                    Text("Oferta: $${producto.precioEnOferta}", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                                }
                                Text("Stock: ${producto.stock}", style = MaterialTheme.typography.bodySmall)
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { onEditarClick(producto) } // U (Update)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(
                                    onClick = {
                                        productoAEliminar = producto // Mostrar diálogo de confirmación
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red) // D (Delete)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
