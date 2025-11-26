package com.example.milsaboresapp.model

data class Producto(
    val id: String,
    val sku: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val precioEnOferta: Double,
    val imageUrl: String = "",
    val categoriaId: String,
    val categoria: String,
    val enOferta: Boolean,
    val destacado: Boolean,
    val stock: Int,
    val stockMinimo: Int = 5
)