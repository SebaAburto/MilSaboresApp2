package com.example.milsaboresapp.repository

import com.example.milsaboresapp.data.local.ProductosSource
import com.example.milsaboresapp.model.Producto

// 🚨 La interfaz solo debe contener las firmas de los métodos.
interface ProductRepository {

    // 1. Obtiene un producto por SKU (necesario para la pantalla de detalle)
    fun obtenerProductoPorSku(sku: String): Producto?

    // 2. Obtiene todos los productos (o por categoría)
    fun getProductosPorCategoria(categoria: String): List<Producto>

    // 3. Genera la lista de categorías para los filtros.
    fun getCategorias(): List<String>
}
class ProductRepositoryImpl : ProductRepository {

    private val productosData: List<Producto> = ProductosSource.productos

    // La función 'obtenerTodosLosProductos' no existe en la interfaz ProductRepository
    // por lo que fue eliminada o reemplazada por getProductosPorCategoria.

    // Implementación de obtenerProductoPorSku (necesario para DetalleScreen)
    override fun obtenerProductoPorSku(sku: String): Producto? {
        return productosData.find { it.sku == sku }
    }

    // 1. FUNCIÓN DE FILTRADO (Implementación)
    override fun getProductosPorCategoria(categoria: String): List<Producto> {
        // Lógica clave: Si la categoría es "todos", devuelve todos.
        if (categoria.equals("todos", ignoreCase = true)) {
            return productosData // Usamos productosData que ya es la lista de ProductosSource
        }

        // Si no es "todos", filtra por la categoría real.
        return productosData.filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
    }

    // 2. FUNCIÓN DE CATEGORÍAS (Implementación)
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
}