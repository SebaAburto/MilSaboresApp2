package com.example.milsaboresapp.repository

import com.example.milsaboresapp.model.Producto

interface ProductRepository {
    // Lectura
    suspend fun getProductos(): List<Producto>
    suspend fun getProductoPorId(id: String): Producto?
    suspend fun obtenerProductoPorSku(sku: String): Producto?
    suspend fun getProductosPorCategoria(categoria: String): List<Producto>
    suspend fun getCategorias(): List<String>

    // Escritura (CRUD)
    suspend fun addProducto(producto: Producto)
    suspend fun updateProducto(producto: Producto)
    suspend fun deleteProducto(producto: Producto)
}