package com.example.milsaboresapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milsaboresapp.ui.components.AppTopBar
import com.example.milsaboresapp.ui.components.DrawerMenu
import com.example.milsaboresapp.ui.screens.HomeScreen
import com.example.milsaboresapp.ui.screens.ProductosScreen
import kotlinx.coroutines.launch

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
                }
            )
        }
    ) {
        Scaffold(
            topBar = { AppTopBar(scope, drawerState) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplica correctamente el padding de Scaffold
            ) {
                composable("home") {
                    HomeScreen(navigateToProductos = { navController.navigate("productos") })
                }
                composable("productos") {
                    ProductosScreen()
                }
            }
        }
    }
}
