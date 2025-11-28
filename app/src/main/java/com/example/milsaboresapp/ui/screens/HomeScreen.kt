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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.R
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.repository.ProductRepositoryImpl
import com.example.milsaboresapp.ui.components.ProductCard
import com.example.milsaboresapp.viewmodel.ProductViewModel
import com.example.milsaboresapp.viewmodel.factory.ProductViewModelFactory

@Composable
fun HomeScreen(
    navigateToProductos: () -> Unit,
    onNavigateToProductDetail: (Producto) -> Unit
) {
    // 1. Instanciamos el ViewModel para obtener los datos reales
    val productRepository = remember { ProductRepositoryImpl() }
    val viewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(productRepository)
    )

    // 2. Escuchamos la lista de productos desde Firebase
    val todosLosProductos by viewModel.productos.collectAsState()

    // 3. Filtramos SOLO los destacados
    // Usamos 'remember' para no recalcular esto en cada frame, solo cuando cambie la lista
    val productosDestacados = remember(todosLosProductos) {
        todosLosProductos.filter { it.destacado }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Banner Principal
        Image(
            painter = painterResource(id = R.drawable.cincuentaanios),
            contentDescription = "Logo App",
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            contentScale = ContentScale.FillWidth
        )

        // Título Destacados
        // Solo mostramos esta sección si hay productos destacados
        if (productosDestacados.isNotEmpty()) {
            Text(
                "¡Productos destacados!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 25.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // 4. LazyRow con los datos REALES
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp), // Un poco de espacio a los lados
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(productosDestacados) { producto ->
                    // Caja para limitar el ancho de cada tarjeta en el carrusel
                    Box(modifier = Modifier.width(220.dp)) {
                        ProductCard(
                            producto = producto,
                            onProductClick = onNavigateToProductDetail
                        )
                    }
                }
            }
        } else {
            // Opcional: Mostrar un texto o loading si no hay destacados aún
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                if (todosLosProductos.isEmpty()) {
                    CircularProgressIndicator() // Cargando...
                } else {
                    Text("No hay productos destacados por ahora.")
                }
            }
        }

        Button(
            onClick = navigateToProductos,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp) // Espacio al final
        ) {
            Text("Ver todos los productos!")
        }
    }
}