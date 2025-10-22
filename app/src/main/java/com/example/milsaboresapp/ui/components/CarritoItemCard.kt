package com.example.milsaboresapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.model.ItemCarrito
import com.example.milsaboresapp.R


@Composable
fun CarritoItemCard(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Image(

                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Foto de ${item.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                // Nombre del producto
                Text(
                    item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )


                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    IconButton(
                        onClick = onDecrementar,
                        enabled = item.cantidad > 0
                    ) {
                        Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Decrementar cantidad")
                    }

                    // Cantidad
                    Text(
                        "${item.cantidad}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )


                    IconButton(onClick = onIncrementar) {
                        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Incrementar cantidad")
                    }

                    // Espacio y Botón Eliminar
                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = onEliminar,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar ítem del carrito")
                    }
                }
            }
        }
    }
}