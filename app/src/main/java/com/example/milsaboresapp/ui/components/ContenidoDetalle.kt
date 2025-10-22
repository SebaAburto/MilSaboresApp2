package com.example.milsaboresapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.ui.theme.ColorBackground
import com.example.milsaboresapp.ui.theme.ColorPrimary
import com.example.milsaboresapp.ui.theme.ColorPrimaryDark
import com.example.milsaboresapp.ui.theme.ColorText
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ContenidoDetalle(
    producto: Producto,
    cantidad: Int,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onAgregarCarrito: () -> Unit
) {
    val context = LocalContext.current
    val imageName = producto.imageUrl
    val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }
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
            .padding(horizontal = 16.dp)
    ) {

        // Categoría
        if (producto.categoria.isNotEmpty()) {
            Text(
                text = producto.categoria.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = ColorText,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp) // Añadimos padding top después de la imagen
            )
        }

        // Nombre, Precio, Descripción
        Text(
            text = producto.nombre,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            ),
            color = ColorPrimaryDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = precioFormateado,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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

        Spacer(modifier = Modifier.height(16.dp))

        // Control de Cantidad
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
                // Botón Decrementar
                IconButton(onClick = onDecrementar, enabled = cantidad > 1) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrementar")
                }

                // Cantidad (NO editable)
                Text(
                    text = cantidad.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                // Botón Incrementar
                IconButton(onClick = onIncrementar) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Incrementar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAgregarCarrito,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text("Añadir al carrito ($cantidad)", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(0.dp))
    }
}