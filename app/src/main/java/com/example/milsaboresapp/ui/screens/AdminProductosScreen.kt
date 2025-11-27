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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen(
    viewModel: ProductViewModel, // Inyectado desde el NavigationWrapper con la Factory
    onAgregarClick: () -> Unit,
    onEditarClick: (Producto) -> Unit
) {
    // Observamos los estados del ViewModel (Flows)
    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    // Estado para el diálogo de eliminación
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    // DIÁLOGO DE CONFIRMACIÓN DE BORRADO
    if (productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de eliminar '${productoAEliminar?.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        productoAEliminar?.let { prod ->
                            viewModel.deleteProducto(prod) // El VM se encarga de sacar el ID
                        }
                        productoAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
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
            // --- FILTRO DE CATEGORÍAS ---
            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = categoriaSeleccionada.uppercase(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filtrar por Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir")
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    // Opción "Todos"
                    DropdownMenuItem(
                        text = { Text("TODOS") },
                        onClick = {
                            viewModel.setCategoria("todos")
                            expanded = false
                        }
                    )
                    // Categorías dinámicas desde el Backend
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.uppercase()) },
                            onClick = {
                                viewModel.setCategoria(categoria)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // --- LISTA DE PRODUCTOS ---
            if (productos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos disponibles", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(productos) { producto ->
                        ProductoAdminItem(
                            producto = producto,
                            onEdit = { onEditarClick(producto) },
                            onDelete = { productoAEliminar = producto }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoAdminItem(
    producto: Producto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("SKU: ${producto.sku}", style = MaterialTheme.typography.bodySmall)
                Text("Cat: ${producto.categoria}", style = MaterialTheme.typography.bodySmall)
                Text("Stock: ${producto.stock}", style = MaterialTheme.typography.bodySmall, color = if((producto.stock ?: 0) < (producto.stockMinimo ?: 5)) Color.Red else Color.Unspecified)
                Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodySmall)
            }

            // Botones de Acción
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}