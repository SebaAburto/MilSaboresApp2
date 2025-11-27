package com.example.milsaboresapp.model

import com.google.gson.annotations.SerializedName

data class Producto(

    var id: String? = null,

    val sku: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,

    // SerializedName: Backend envía "precioOferta" -> App usa "precioEnOferta"
    @SerializedName("precioOferta")
    val precioEnOferta: Double,

    @SerializedName("imagen")
    val imageUrl: String = "",

    val categoriaId: String? = null,

    @SerializedName("categoriaNombre")
    val categoria: String,

    val enOferta: Boolean,
    val destacado: Boolean,
    val stock: Int,
    val stockMinimo: Int = 5
)