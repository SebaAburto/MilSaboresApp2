package com.example.milsaboresapp.model



data class ItemCarrito(
    val sku: String,
    val nombre: String,
    val precio: Double,
    val cantidad: Int = 1,
)