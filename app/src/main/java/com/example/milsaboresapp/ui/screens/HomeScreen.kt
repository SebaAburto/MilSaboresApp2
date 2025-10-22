package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.remote.ProductosSource
import com.example.milsaboresapp.ui.components.ProductCard
import com.example.milsaboresapp.ui.theme.ColorLight


@Composable
fun HomeScreen(navigateToProductos: () -> Unit) {

    val productosDestacados = ProductosSource.productos.filter { it.destacado }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorLight)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.cincuentaanios),
            contentDescription = "Logo App",
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            contentScale = ContentScale.FillWidth
        )

        Text("¡Productos destacados!",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 25.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        LazyRow {
            items(productosDestacados) { producto ->
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(5.dp)// define el ancho de cada tarjeta
                ) {
                    ProductCard(producto = producto)
                }
            }
        }

        Button(
            onClick = navigateToProductos,
            modifier = Modifier.align(Alignment.CenterHorizontally)

        ) {
            Text("Ver todos los productos!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen {}
}
