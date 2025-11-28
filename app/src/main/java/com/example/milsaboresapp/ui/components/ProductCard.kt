package com.example.milsaboresapp.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsaboresapp.R
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.util.ImageUtil
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ProductCard(
    producto: Producto,
    modifier: Modifier = Modifier,
    onProductClick: (Producto) -> Unit
) {
    val context = LocalContext.current

    // Lógica de Imagen
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

    // Lógica de Stock Bajo
    val stockActual = producto.stock ?: 0
    val stockMin = producto.stockMinimo ?: 5
    val esStockBajo = stockActual <= stockMin && stockActual > 0
    val agotado = stockActual == 0

    // Cálculo del Porcentaje de Descuento (Visual)
    val porcentajeDescuento = if (producto.enOferta && (producto.precio ?: 0.0) > 0) {
        val precioNormal = producto.precio ?: 0.0
        val precioOferta = producto.precioEnOferta ?: 0.0
        ((1 - (precioOferta / precioNormal)) * 100).roundToInt()
    } else {
        0
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onProductClick(producto) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // CAJA DE IMAGEN (Para poner badges encima)
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
                        .height(200.dp)
                )

                // BADGE DE OFERTA (ej: -20%)
                if (producto.enOferta && porcentajeDescuento > 0) {
                    Surface(
                        color = Color.Red,
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "-$porcentajeDescuento%",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // BADGE DE AGOTADO
                if (agotado) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.matchParentSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("AGOTADO", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = producto.nombre ?: "Sin Nombre",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = producto.categoria?.uppercase() ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ALERTA DE STOCK BAJO
                if (esStockBajo) {
                    Text(
                        text = "¡POR AGOTARSE! ($stockActual)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // PRECIOS
                val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

                if (producto.enOferta) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Precio Normal Tachado
                        Text(
                            text = formatter.format(producto.precio),
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Precio Oferta Grande
                        Text(
                            text = formatter.format(producto.precioEnOferta),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Precio Normal Único
                    Text(
                        text = formatter.format(producto.precio),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}