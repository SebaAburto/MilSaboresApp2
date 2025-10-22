package com.example.milsaboresapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milsaboresapp.ui.components.AppTopBar
import com.example.milsaboresapp.ui.components.DrawerMenu
import com.example.milsaboresapp.ui.screens.FormularioScreen
import com.example.milsaboresapp.ui.screens.HomeScreen
import com.example.milsaboresapp.ui.screens.ProductosScreen
import com.example.milsaboresapp.ui.screens.CarritoScreen
import com.example.milsaboresapp.ui.screens.ProductoDetalleScreen
import kotlinx.coroutines.launch
import androidx.navigation.NavType
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                    scope.launch { drawerState.close() }
                },
                onNavigateToProductos = {
                    navController.navigate("productos")
                    scope.launch { drawerState.close() }
                },
                onNavigateToRegistro = {
                    navController.navigate("registro")
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    scope,
                    drawerState,
                    onNavigateToCarrito = { navController.navigate("carrito") })
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                composable("home") {
                    HomeScreen(
                        navigateToProductos = { navController.navigate("productos") },
                        onNavigateToProductDetail = { producto ->
                            navController.navigate("product_detail_route/${producto.sku}")
                        }
                    )
                }
                composable("productos") {
                    ProductosScreen(
                        navController = navController,
                        onNavigateToProductDetail = { producto ->
                            navController.navigate("product_detail_route/${producto.sku}")
                        })
                }
                composable("registro") {
                    FormularioScreen()
                }
                composable("carrito") {
                    CarritoScreen()
                }

                // BLOQUE CORREGIDO: RUTA DE DETALLE
                composable(
                    // 1. RUTA: Define el argumento como '{productoSku}' para coincidir con la pantalla (o usa 'sku')
                    route = "product_detail_route/{productoSku}",
                    arguments = listOf(navArgument("productoSku") { type = NavType.StringType })
                ) { backStackEntry ->

                    // 2. EXTRACCIÓN: Obtén el valor usando el nombre del argumento definido en 'navArgument'
                    val productoSku = backStackEntry.arguments?.getString("productoSku")

                    // 3. LLAMADA: Llama a la pantalla solo si el SKU existe.
                    if (productoSku != null) {
                        ProductoDetalleScreen(
                            navController = navController,
                            productoSku = productoSku
                        )
                    } else {
                        // Opcional: Mostrar un error o navegar de vuelta si el SKU es nulo
                        Text("Error: Producto no especificado")
                    }
                }
            }
        }
    }
}