package com.example.milsaboresapp.navigation

//ARCHIVO NO USADO EN LA VERSION FINAL, se hubiera usado para "comprimir" y "descomprimir"
//los datos para que fueran mas faciles de almacenar y transmitir (en Json)

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