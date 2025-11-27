package com.example.milsaboresapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.milsaboresapp.repository.ProductRepositoryImpl
import com.example.milsaboresapp.ui.components.AppTopBar
import com.example.milsaboresapp.ui.components.DrawerMenu
import com.example.milsaboresapp.ui.screens.*
import com.example.milsaboresapp.viewmodel.factory.ProductViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // --- 🎯 INICIALIZACIÓN DE DEPENDENCIAS ---

    // 1. Repositorio (Sin argumentos ahora)
    val productRepository = remember { ProductRepositoryImpl() }

    // 2. Factory para el ViewModel
    val productViewModelFactory = remember {
        ProductViewModelFactory(productRepository)
    }
    // --- FIN DE INICIALIZACIÓN ---

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
                },
                onNavigateToConfiguracion = {
                    navController.navigate("configuracion")
                    scope.launch { drawerState.close() }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                    scope.launch { drawerState.close() }
                },
                onNavigateToNosotros = {
                    navController.navigate("nosotros")
                    scope.launch { drawerState.close() }
                },
                onNavigateToAdminProductosScreen = {
                    navController.navigate("AdminProductosScreen")
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
                // ... (Rutas home, registro, login, etc. siguen igual) ...
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
                composable("registro") { FormularioScreen() }
                composable("login") { LoginScreen() }
                composable("carrito") { CarritoScreen() }
                composable("configuracion") { ConfiguracionScreen() }
                composable("nosotros") { NosotrosScreen() }

                // --- RUTAS CRUD ---

                // Lista Admin
                composable("AdminProductosScreen") {
                    AdminProductosScreen(
                        viewModel = viewModel(factory = productViewModelFactory),
                        onAgregarClick = { navController.navigate("crearProducto") },
                        onEditarClick = { producto ->
                            producto.id?.let { id -> navController.navigate("editarProducto/$id") }
                        }
                    )
                }

                // Crear
                composable("crearProducto") {
                    ProductoFormScreen(
                        viewModel = viewModel(factory = productViewModelFactory),
                        productoIdEditar = null,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Editar
                composable(
                    route = "editarProducto/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    ProductoFormScreen(
                        viewModel = viewModel(factory = productViewModelFactory),
                        productoIdEditar = id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Detalle
                composable(
                    route = "product_detail_route/{productoSku}",
                    arguments = listOf(navArgument("productoSku") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productoSku = backStackEntry.arguments?.getString("productoSku")
                    if (productoSku != null) {
                        ProductoDetalleScreen(
                            navController = navController,
                            productoSku = productoSku
                        )
                    } else {
                        Text("Error: Producto no especificado")
                    }
                }
            }
        }
    }
}