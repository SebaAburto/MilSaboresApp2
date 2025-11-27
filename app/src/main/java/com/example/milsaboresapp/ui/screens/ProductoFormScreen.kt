package com.example.milsaboresapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: ProductViewModel, // Inyectado
    productoIdEditar: String? = null, // Si es NULL = CREAR. Si tiene ID = EDITAR.
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Observamos estados del ViewModel
    val operacionExitosa by viewModel.operacionExitosa.collectAsState()
    val productoCargado by viewModel.productoObtenido.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    // --- VARIABLES DE FORMULARIO ---
    var sku by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var precioOferta by remember { mutableStateOf("0.0") }
    var stock by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("5") }
    var categoriaNombre by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var enOferta by remember { mutableStateOf(false) }
    var destacado by remember { mutableStateOf(false) }

    // 1. CARGAR DATOS SI ES EDICIÓN
    LaunchedEffect(productoIdEditar) {
        if (productoIdEditar != null) {
            viewModel.obtenerProductoPorId(productoIdEditar)
        } else {
            viewModel.resetOperacion() // Limpiar estados viejos si entramos a crear
        }
    }

    // 2. LLENAR FORMULARIO CUANDO LLEGAN LOS DATOS
    LaunchedEffect(productoCargado) {
        productoCargado?.let { prod ->
            sku = prod.sku
            nombre = prod.nombre
            descripcion = prod.descripcion
            precio = prod.precio?.toString() ?: ""
            precioOferta = prod.precioEnOferta?.toString() ?: "0.0"
            stock = prod.stock?.toString() ?: ""
            stockMinimo = prod.stockMinimo?.toString() ?: "5"
            // OJO: Usamos 'categoria' (nombre) para que se vea en el TextField
            categoriaNombre = prod.categoria ?: ""
            imageUrl = prod.imageUrl ?: ""
            enOferta = prod.enOferta
            destacado = prod.destacado
        }
    }

    // 3. MANEJAR ÉXITO Y SALIR
    LaunchedEffect(operacionExitosa) {
        if (operacionExitosa) {
            Toast.makeText(context, "Operación exitosa", Toast.LENGTH_SHORT).show()
            viewModel.resetOperacion()
            onNavigateBack()
        }
    }

    // 4. MANEJAR ERRORES
    LaunchedEffect(mensajeError) {
        mensajeError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetOperacion() // Limpiamos el error para no mostrarlo infinito
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productoIdEditar == null) "Nuevo Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // SKU y Nombre
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría (Texto libre: Tu backend resuelve si existe o crea nueva)
            OutlinedTextField(
                value = categoriaNombre,
                onValueChange = { categoriaNombre = it },
                label = { Text("Categoría (Ej: Tortas)") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Si la cambias, el producto se moverá automáticamente.") }
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Precio y Stock
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // Stock Mínimo
            OutlinedTextField(
                value = stockMinimo,
                onValueChange = { stockMinimo = it },
                label = { Text("Stock Mínimo (Alerta)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Imagen URL
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Nombre imagen (res) o URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Switches
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Destacado", modifier = Modifier.weight(1f))
                Switch(checked = destacado, onCheckedChange = { destacado = it })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("En Oferta", modifier = Modifier.weight(1f))
                Switch(checked = enOferta, onCheckedChange = { enOferta = it })
            }

            if (enOferta) {
                OutlinedTextField(
                    value = precioOferta,
                    onValueChange = { precioOferta = it },
                    label = { Text("Precio Oferta") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Validaciones básicas
                    if (nombre.isBlank() || sku.isBlank() || precio.isBlank()) {
                        Toast.makeText(context, "Nombre, SKU y Precio son obligatorios", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Armamos el objeto
                    val productoFinal = Producto(
                        id = productoIdEditar, // Si es null, el backend lo ignora al crear
                        sku = sku,
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        precioEnOferta = precioOferta.toDoubleOrNull() ?: 0.0,
                        stock = stock.toIntOrNull() ?: 0,
                        stockMinimo = stockMinimo.toIntOrNull() ?: 5,
                        categoria = categoriaNombre,
                        imageUrl = imageUrl,
                        enOferta = enOferta,
                        destacado = destacado
                    )

                    if (productoIdEditar == null) {
                        viewModel.addProducto(productoFinal)
                    } else {
                        // Al editar enviamos ID y Producto
                        viewModel.updateProducto(productoFinal)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productoIdEditar == null) "Crear Producto" else "Guardar Cambios")
            }
        }
    }
}