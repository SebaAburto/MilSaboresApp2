package com.example.milsaboresapp.repository

import com.example.milsaboresapp.data.local.ProductosSource
import com.example.milsaboresapp.model.Producto


interface ProductRepository {

    // Necesario para la pantalla de detalle
    fun obtenerProductoPorSku(sku: String): Producto?

    // Obtiene todos los productos (o por categoría)
    fun getProductosPorCategoria(categoria: String): List<Producto>

    // Genera la lista de categorías para los filtros.
    fun getCategorias(): List<String>

    // --- 🎯 OPERACIONES CRUD AÑADIDAS ---
    fun addProducto(producto: Producto)
    fun updateProducto(producto: Producto)
    fun deleteProducto(sku: String)
}

class ProductRepositoryImpl : ProductRepository {

    // CAMBIO CLAVE 1: Usamos una lista mutable para simular la persistencia
    private val productosData: MutableList<Producto> = ProductosSource.productos.toMutableList()

    override fun obtenerProductoPorSku(sku: String): Producto? {
        return productosData.find { it.sku == sku }
    }

    override fun getProductosPorCategoria(categoria: String): List<Producto> {

        if (categoria.equals("todos", ignoreCase = true)) {
            // Devolvemos una copia inmutable para evitar modificaciones externas
            return productosData.toList()
        }

        // Filtra por la categoría real.
        return productosData.filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
    }

    // FUNCIÓN DE CATEGORÍAS (Implementación)
    override fun getCategorias(): List<String> {
        // Obtiene todas las categorías únicas del Source.
        val categoriasUnicas = productosData
            .map { it.categoria }
            .distinct()
            .toMutableList()

        // Inserta la opción "todos" al principio.
        categoriasUnicas.add(0, "todos")
        return categoriasUnicas
    }

    // --- 🎯 IMPLEMENTACIÓN DE OPERACIONES CRUD ---

    // CREATE (C): Añade un nuevo producto a la lista mutable.
    override fun addProducto(producto: Producto) {
        // En una app real, aquí se llamaría a la base de datos (ej: RoomDao.insert(producto))
        productosData.add(producto)
    }

    // UPDATE (U): Busca y reemplaza el producto por su SKU.
    override fun updateProducto(producto: Producto) {
        val index = productosData.indexOfFirst { it.sku == producto.sku }
        if (index != -1) {
            // En una app real, aquí se llamaría a la base de datos (ej: RoomDao.update(producto))
            productosData[index] = producto
        }
    }

    // DELETE (D): Elimina un producto por su SKU.
    override fun deleteProducto(sku: String) {
        // En una app real, aquí se llamaría a la base de datos (ej: RoomDao.deleteBySku(sku))
        productosData.removeIf { it.sku == sku }
    }
}