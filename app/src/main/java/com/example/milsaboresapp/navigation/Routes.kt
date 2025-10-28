package com.example.milsaboresapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Productos

@Serializable
object Registro

@Serializable
object Nosotros

@Serializable
data class ProductoDetalle(val productoSku: String)

@Serializable
object Carrito