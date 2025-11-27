package com.example.milsaboresapp.network

import com.example.milsaboresapp.model.Producto
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @GET("api/productos")
    suspend fun listarProductos(): List<Producto>

    @GET("api/productos/{id}")
    suspend fun obtenerProducto(@Path("id") id: String): Producto

    // Usamos ResponseBody para evitar el error de parseo de texto plano
    @POST("api/productos/guardar")
    suspend fun guardarProducto(@Body producto: Producto): ResponseBody

    @PUT("api/productos/editar/{id}")
    suspend fun editarProducto(@Path("id") id: String, @Body producto: Producto): ResponseBody

    @DELETE("api/productos/eliminar/{id}")
    suspend fun eliminarProducto(@Path("id") id: String): ResponseBody
}