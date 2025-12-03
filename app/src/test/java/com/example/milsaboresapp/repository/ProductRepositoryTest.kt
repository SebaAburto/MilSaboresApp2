package com.example.milsaboresapp.repository

import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.network.ApiService
import com.example.milsaboresapp.network.RetrofitClient

class ProductRepositoryImpl(
    private val api: ApiService = RetrofitClient.instance
) : ProductRepository {

    override suspend fun getProductos(): List<Producto> = try { api.listarProductos() } catch (e: Exception) { emptyList() }

    override suspend fun getProductoPorId(id: String): Producto? = try { api.obtenerProducto(id) } catch (e: Exception) { null }

    override suspend fun obtenerProductoPorSku(sku: String): Producto? = getProductos().find { it.sku == sku }

    override suspend fun getProductosPorCategoria(categoria: String): List<Producto> {
        val lista = getProductos()
        return if (categoria.equals("todos", ignoreCase = true)) lista else lista.filter { it.categoria.equals(categoria, ignoreCase = true) }
    }

    override suspend fun getCategorias(): List<String> {
        val lista = getProductos()
        val cats = lista.mapNotNull { it.categoria }.filter { it.isNotBlank() }.distinct().sorted().toMutableList()
        cats.add(0, "todos")
        return cats
    }

    override suspend fun addProducto(producto: Producto) {
        val response = api.guardarProducto(producto)
        response.string()
    }

    override suspend fun updateProducto(producto: Producto) {
        val id = producto.id ?: throw Exception("El producto no tiene ID")
        val response = api.editarProducto(id, producto)
        response.string()
    }

    override suspend fun deleteProducto(producto: Producto) {
        val id = producto.id ?: throw Exception("El producto no tiene ID")
        val response = api.eliminarProducto(id)
        response.string()
    }
}