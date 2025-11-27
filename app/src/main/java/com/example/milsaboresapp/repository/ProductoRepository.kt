package com.example.milsaboresapp.repository

import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.network.RetrofitClient

// 1. LA INTERFAZ
interface ProductRepository {
    suspend fun obtenerProductoPorSku(sku: String): Producto?
    suspend fun getProductos(): List<Producto>
    suspend fun getProductosPorCategoria(categoria: String): List<Producto>
    suspend fun getCategorias(): List<String>

    // --- ESTA ES LA QUE FALTABA ---
    suspend fun getProductoPorId(id: String): Producto?
    // -----------------------------

    suspend fun addProducto(producto: Producto)
    suspend fun updateProducto(producto: Producto)
    suspend fun deleteProducto(producto: Producto)
}

// 2. LA IMPLEMENTACIÓN
class ProductRepositoryImpl : ProductRepository {

    private val api = RetrofitClient.instance

    override suspend fun getProductos(): List<Producto> {
        return try {
            api.listarProductos()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // --- IMPLEMENTACIÓN DE LA FUNCIÓN FALTANTE ---
    override suspend fun getProductoPorId(id: String): Producto? {
        return try {
            // Llama al endpoint @GET("api/productos/{id}")
            api.obtenerProducto(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    // ---------------------------------------------

    override suspend fun obtenerProductoPorSku(sku: String): Producto? {
        // Como tu backend busca por ID, para buscar por SKU
        // traemos la lista y filtramos (o podrías crear un endpoint específico en Spring)
        val lista = getProductos()
        return lista.find { it.sku == sku }
    }

    override suspend fun getProductosPorCategoria(categoria: String): List<Producto> {
        val lista = getProductos()
        if (categoria.equals("todos", ignoreCase = true)) return lista
        return lista.filter { it.categoria.equals(categoria, ignoreCase = true) }
    }

    override suspend fun getCategorias(): List<String> {
        val lista = getProductos()
        val categorias = lista.mapNotNull { it.categoria }.distinct().toMutableList()
        categorias.add(0, "todos")
        return categorias
    }

    override suspend fun addProducto(producto: Producto) {
        try {
            api.guardarProducto(producto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-lanzamos para que el ViewModel sepa que falló
        }
    }

    override suspend fun updateProducto(producto: Producto) {
        try {
            val id = producto.id ?: throw Exception("ID nulo al editar")
            api.editarProducto(id, producto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun deleteProducto(producto: Producto) {
        try {
            val id = producto.id ?: throw Exception("ID nulo al eliminar")
            api.eliminarProducto(id)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}