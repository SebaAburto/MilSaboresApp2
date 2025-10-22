package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.remote.ProductosSource
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.ui.theme.ColorBackground
import com.example.milsaboresapp.ui.theme.ColorPrimary
import com.example.milsaboresapp.ui.theme.ColorPrimaryDark
import com.example.milsaboresapp.ui.theme.ColorText
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.WindowInsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreen(navController: NavController, sku: String?) {
    val producto = ProductosSource.productos.find { it.sku == sku }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Producto",
                        style = MaterialTheme.typography.titleLarge,
                        color = ColorText // Título en marrón oscuro
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
                ),windowInsets = WindowInsets(0.dp) //Lo usé para apegar mas la barra de volver al topbar
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ColorBackground)
                .verticalScroll(rememberScrollState())
        ) {

            if (producto != null) {
                DetalleContent(producto = producto)
            } else {
                Text(
                    text = "¡Lo sentimos! Producto no encontrado.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun DetalleContent(producto: Producto) {
    val context = LocalContext.current
    val imageName = producto.imageUrl
    val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val precioFormateado = formatter.format(producto.precio)


    // Sección de Imagen
    Image(
        painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.logo),
        contentDescription = "Imagen de ${producto.nombre}",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    )

    // Sección de Detalle
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorBackground)
            .padding(16.dp)
    ) {

        // Categoría
        if (producto.categoria.isNotEmpty()) {
            Text(
                text = producto.categoria.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = ColorText,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }


        Text(
            text = producto.nombre,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            ),
            color = ColorPrimaryDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Precio
        Text(
            text = precioFormateado,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Título de la descripción
        Text(
            text = "Descripción del Producto:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorText,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Descripción
        Text(
            text = producto.descripcion,
            style = MaterialTheme.typography.bodyLarge,
            color = ColorText
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}