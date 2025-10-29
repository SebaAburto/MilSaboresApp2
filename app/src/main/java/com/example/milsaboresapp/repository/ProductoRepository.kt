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
}
class ProductRepositoryImpl : ProductRepository {

    private val productosData: List<Producto> = ProductosSource.productos

    override fun obtenerProductoPorSku(sku: String): Producto? {
        return productosData.find { it.sku == sku }
    }

    override fun getProductosPorCategoria(categoria: String): List<Producto> {

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