package com.example.milsaboresapp.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsaboresapp.R
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.util.ImageUtil
import com.example.milsaboresapp.ui.theme.*
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ContenidoDetalle(
    producto: Producto,
    cantidad: Int,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onAgregarCarrito: () -> Unit
) {
    val context = LocalContext.current

    // Stock
    val stockActual = producto.stock ?: 0
    val stockMin = producto.stockMinimo ?: 5
    val esStockBajo = stockActual <= stockMin && stockActual > 0
    val agotado = stockActual == 0

    // Oferta (Cálculo del porcentaje para mostrar)
    val porcentajeDescuento = if (producto.enOferta && (producto.precio ?: 0.0) > 0) {
        val precioNormal = producto.precio ?: 0.0
        val precioOferta = producto.precioEnOferta ?: 0.0
        ((1 - (precioOferta / precioNormal)) * 100).roundToInt()
    } else {
        0
    }

    // Imagen
    val modelData = remember(producto.imageUrl) {
        val url = producto.imageUrl ?: ""
        when {
            url.startsWith("data:image") -> ImageUtil.base64ToBitmap(url)
            url.contains("content://") || url.contains("http") || url.contains("android.resource") -> Uri.parse(url)
            else -> {
                val resId = context.resources.getIdentifier(url, "drawable", context.packageName)
                if (resId != 0) resId else R.drawable.logo
            }
        }
    }

    // Formateador de dinero
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    // CAJA DE IMAGEN CON BADGES
    Box {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(modelData)
                .crossfade(true)
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .build(),
            contentDescription = "Imagen de ${producto.nombre}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        // Badge: AGOTADO
        if (agotado) {
            Surface(
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp) // Cubre toda la imagen
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("AGOTADO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                }
            }
        } else if (producto.enOferta && porcentajeDescuento > 0) {
            // Badge: OFERTA (ej: -20%)
            Surface(
                color = Color.Red,
                shape = RoundedCornerShape(bottomStart = 16.dp),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = "-$porcentajeDescuento%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorBackground)
            .padding(horizontal = 16.dp)
    ) {
        // Categoría
        if (producto.categoria?.isNotEmpty() == true) {
            Text(
                text = producto.categoria!!.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = ColorText,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
        }

        // Nombre
        Text(
            text = producto.nombre,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            ),
            color = ColorPrimaryDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // PRECIOS (Lógica de Oferta)
        if (producto.enOferta) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Precio Anterior Tachado
                Text(
                    text = formatter.format(producto.precio),
                    style = MaterialTheme.typography.titleLarge.copy(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 12.dp)
                )
                // Precio Oferta Destacado
                Text(
                    text = formatter.format(producto.precioEnOferta),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Red
                )
            }
        } else {
            // Precio Normal
            Text(
                text = formatter.format(producto.precio),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                color = ColorPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // ALERTA STOCK
        if (esStockBajo && !agotado) {
            Text(
                text = "¡Apúrate! Quedan pocas unidades ($stockActual disponibles)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Descripción del Producto:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = producto.descripcion,
            style = MaterialTheme.typography.bodyLarge,
            color = ColorText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // CONTROLES DE CANTIDAD (Ocultar si está agotado)
        if (!agotado) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Cantidad:", style = MaterialTheme.typography.titleMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onDecrementar, enabled = cantidad > 1) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrementar")
                    }
                    Text(
                        text = cantidad.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    // Bloquear incremento si alcanza el stock máximo
                    IconButton(onClick = onIncrementar, enabled = cantidad < stockActual) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Incrementar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN DE ACCIÓN
        Button(
            onClick = onAgregarCarrito,
            enabled = !agotado, // Deshabilitar si no hay stock
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (agotado) Color.Gray else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (agotado) "AGOTADO" else "Añadir al carrito ($cantidad)",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}