package com.example.milsaboresapp.model

data class Producto(
    val sku: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val precio: Double,
    val destacado: Boolean,
    val imageUrl: String = ""
)