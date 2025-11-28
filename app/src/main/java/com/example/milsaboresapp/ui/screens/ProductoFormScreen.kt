package com.example.milsaboresapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.ui.components.ImageSelector
import com.example.milsaboresapp.viewmodel.ProductViewModel
import com.example.milsaboresapp.util.ImageUtil
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: ProductViewModel,
    productoIdEditar: String? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val operacionExitosa by viewModel.operacionExitosa.collectAsState()
    val productoCargado by viewModel.productoObtenido.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    // Campos
    var sku by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var porcentajeDescuento by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("5") }
    var categoriaNombre by remember { mutableStateOf("") }
    var enOferta by remember { mutableStateOf(false) }
    var destacado by remember { mutableStateOf(false) }

    // Imágenes
    var selectedImageModel by remember { mutableStateOf<Any?>(null) }
    var uriNuevaParaGuardar by remember { mutableStateOf<Uri?>(null) }
    var imagenOriginalBackend: String? by remember { mutableStateOf(null) }

    LaunchedEffect(productoIdEditar) {
        if (productoIdEditar != null) viewModel.obtenerProductoPorId(productoIdEditar)
        else viewModel.resetOperacion()
    }

    LaunchedEffect(productoCargado) {
        productoCargado?.let { prod ->
            sku = prod.sku ?: ""
            nombre = prod.nombre ?: ""
            descripcion = prod.descripcion ?: ""
            precio = prod.precio?.toString() ?: ""
            stock = prod.stock?.toString() ?: ""
            stockMinimo = prod.stockMinimo?.toString() ?: "5"
            categoriaNombre = prod.categoria ?: ""
            enOferta = prod.enOferta
            destacado = prod.destacado
            imagenOriginalBackend = prod.imageUrl

            // CALCULAR PORCENTAJE AL EDITAR (Ingeniería inversa)
            if (prod.enOferta && (prod.precio ?: 0.0) > 0) {
                val precioNormal = prod.precio ?: 0.0
                val precioOferta = prod.precioEnOferta ?: 0.0
                val pct = ((1 - (precioOferta / precioNormal)) * 100).roundToInt()
                porcentajeDescuento = pct.toString()
            } else {
                porcentajeDescuento = ""
            }

            if (!prod.imageUrl.isNullOrBlank()) {
                val url = prod.imageUrl
                selectedImageModel = when {
                    url.startsWith("data:image") -> ImageUtil.base64ToBitmap(url)
                    url.contains("/") -> Uri.parse(url)
                    else -> {
                        val resId = context.resources.getIdentifier(url, "drawable", context.packageName)
                        if (resId != 0) resId else null
                    }
                }
            }
        }
    }

    LaunchedEffect(operacionExitosa) {
        if (operacionExitosa) {
            Toast.makeText(context, "Operación exitosa", Toast.LENGTH_SHORT).show()
            viewModel.resetOperacion()
            onNavigateBack()
        }
    }
    LaunchedEffect(mensajeError) {
        mensajeError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetOperacion()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productoIdEditar == null) "Nuevo Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Foto del Producto", style = MaterialTheme.typography.labelLarge)
            ImageSelector(
                imageModel = selectedImageModel,
                onUriSelected = { uri ->
                    selectedImageModel = uri
                    uriNuevaParaGuardar = uri
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(sku, { sku = it }, label = { Text("SKU") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(categoriaNombre, { categoriaNombre = it }, label = { Text("Categoría (Ej: Tortas)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(precio, { precio = it }, label = { Text("Precio Normal") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(stock, { stock = it }, label = { Text("Stock") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
            OutlinedTextField(stockMinimo, { stockMinimo = it }, label = { Text("Stock Mínimo Alerta") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Destacado", modifier = Modifier.weight(1f))
                Switch(checked = destacado, onCheckedChange = { destacado = it })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("En Oferta", modifier = Modifier.weight(1f))
                Switch(checked = enOferta, onCheckedChange = { enOferta = it })
            }

            // CAMPO DE PORCENTAJE (Solo aparece si "En Oferta" está activo)
            if (enOferta) {
                OutlinedTextField(
                    value = porcentajeDescuento,
                    onValueChange = {
                        // Validación simple: solo números, max 100
                        if (it.all { char -> char.isDigit() }) {
                            val num = it.toIntOrNull() ?: 0
                            if (num <= 100) porcentajeDescuento = it
                        }
                    },
                    label = { Text("Porcentaje Descuento (0-100%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    suffix = { Text("%") },
                    isError = enOferta && (porcentajeDescuento.toIntOrNull() ?: 0) <= 0,
                    supportingText = {
                        if (enOferta && (porcentajeDescuento.toIntOrNull() ?: 0) <= 0) {
                            Text("Debe ser mayor a 0", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                // Texto de ayuda mostrando el precio final calculado
                if (precio.isNotEmpty() && porcentajeDescuento.isNotEmpty()) {
                    val pNormal = precio.toDoubleOrNull() ?: 0.0
                    val pct = porcentajeDescuento.toDoubleOrNull() ?: 0.0
                    val pFinal = pNormal - (pNormal * (pct / 100))
                    Text(
                        text = "Precio Final: $${pFinal.toInt()}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || sku.isBlank() || precio.isBlank()) {
                        Toast.makeText(context, "Campos vacíos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // CÁLCULO MATEMÁTICO DEL PRECIO DE OFERTA
                    val precioNormalVal = precio.toDoubleOrNull() ?: 0.0
                    var precioOfertaVal = 0.0

                    if (enOferta) {
                        val porcentajeVal = porcentajeDescuento.toDoubleOrNull() ?: 0.0

                        // --- VALIDACIÓN NUEVA ---
                        if (porcentajeVal <= 0) {
                            Toast.makeText(context, "El descuento debe ser mayor a 0%", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        // ------------------------

                        // Restar el porcentaje al precio normal
                        precioOfertaVal = precioNormalVal - (precioNormalVal * (porcentajeVal / 100))
                    }

                    val productoFinal = Producto(
                        id = productoIdEditar,
                        sku = sku,
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precioNormalVal,
                        precioEnOferta = precioOfertaVal, // Guardamos el valor calculado
                        stock = stock.toIntOrNull() ?: 0,
                        stockMinimo = stockMinimo.toIntOrNull() ?: 5,
                        categoria = categoriaNombre,
                        imageUrl = imagenOriginalBackend ?: "",
                        enOferta = enOferta,
                        destacado = destacado
                    )

                    viewModel.guardarProductoConImagen(
                        context = context,
                        producto = productoFinal,
                        imageUri = uriNuevaParaGuardar
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productoIdEditar == null) "Crear Producto" else "Guardar Cambios")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}