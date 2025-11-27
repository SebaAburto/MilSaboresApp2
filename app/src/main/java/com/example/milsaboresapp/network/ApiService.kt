package com.example.milsaboresapp.network

import com.example.milsaboresapp.model.Producto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Listar todos
    @GET("api/productos")
    suspend fun listarProductos(): List<Producto>

    // Obtener uno por ID (Usamos ID de firebase, no SKU, para detalles técnicos)
    @GET("api/productos/{id}")
    suspend fun obtenerProducto(@Path("id") id: String): Producto

    // Guardar (POST)
    @POST("api/productos/guardar")
    suspend fun guardarProducto(@Body producto: Producto): String

    // Editar (PUT) - Nota: Backend espera ID en la URL
    @PUT("api/productos/editar/{id}")
    suspend fun editarProducto(@Path("id") id: String, @Body producto: Producto): String

    // Eliminar (DELETE)
    @DELETE("api/productos/eliminar/{id}")
    suspend fun eliminarProducto(@Path("id") id: String): String
}