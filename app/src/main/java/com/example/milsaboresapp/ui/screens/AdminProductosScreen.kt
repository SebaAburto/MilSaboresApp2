package com.example.milsaboresapp.ui.screens

import android.net.Uri
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsaboresapp.R
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.util.ImageUtil
import com.example.milsaboresapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen(
    viewModel: ProductViewModel,
    onAgregarClick: () -> Unit,
    onEditarClick: (Producto) -> Unit
) {
    // Observamos los estados
    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    // Estado para eliminar
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    // DIÁLOGO DE CONFIRMACIÓN
    if (productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de eliminar '${productoAEliminar?.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        productoAEliminar?.let { prod ->
                            viewModel.deleteProducto(prod)
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
                    // Opción Manual "TODOS"
                    DropdownMenuItem(
                        text = { Text("TODOS") },
                        onClick = {
                            viewModel.setCategoria("todos")
                            expanded = false
                        }
                    )

                    // Opciones Dinámicas (Filtrando "todos" para evitar duplicados)
                    categorias.filter { !it.equals("todos", ignoreCase = true) }.forEach { categoria ->
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
    val context = LocalContext.current

    // LÓGICA DE IMAGEN (Para que se vea la miniatura)
    val modelData = remember(producto.imageUrl) {
        val url = producto.imageUrl ?: ""
        when {
            // A. Base64
            url.startsWith("data:image") -> ImageUtil.base64ToBitmap(url)
            // B. URI / Web
            url.contains("content://") || url.contains("http") || url.contains("android.resource") -> Uri.parse(url)
            // C. Recurso Local
            else -> {
                val resId = context.resources.getIdentifier(url, "drawable", context.packageName)
                if (resId != 0) resId else R.drawable.logo
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp) // Un poco menos de padding para aprovechar espacio
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- MINIATURA DE IMAGEN ---
            Card(
                modifier = Modifier.size(60.dp),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(modelData)
                        .crossfade(true)
                        .error(R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- INFO DE TEXTO ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text("SKU: ${producto.sku}", style = MaterialTheme.typography.bodySmall)
                Text("Cat: ${producto.categoria}", style = MaterialTheme.typography.bodySmall)

                // Stock con alerta roja
                val stock = producto.stock ?: 0
                val stockMin = producto.stockMinimo ?: 5
                Text(
                    text = "Stock: $stock",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (stock <= stockMin) FontWeight.Bold else FontWeight.Normal,
                    color = if (stock == 0) Color.Red else if (stock <= stockMin) Color(0xFFD32F2F) else Color.Unspecified
                )

                Text("Precio: $${producto.precio?.toInt()}", style = MaterialTheme.typography.bodySmall)
            }

            // --- BOTONES DE ACCIÓN ---
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