package com.example.milsaboresapp.repository

import com.example.milsaboresapp.data.remote.ProductosSource
import com.example.milsaboresapp.model.Producto

class ProductRepository {

    // 1. FUNCIÓN DE FILTRADO: Llama a todos o a una categoría específica
    fun getProductosPorCategoria(categoria: String): List<Producto> {
        // Lógica clave: Si la categoría es "all" (o "todos"), devuelve todos.
        if (categoria.equals("all", ignoreCase = true) || categoria.equals("todos", ignoreCase = true)) {
            return ProductosSource.productos
        }

        // Si no es "all" o "todos", filtra por la categoría real.
        return ProductosSource.productos.filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
    }

    // 2. FUNCIÓN DE CATEGORÍAS: Genera la lista de filtros para la UI.
    fun getCategorias(): List<String> {
        // Obtiene todas las categorías únicas del JSON/Source.
        val categoriasUnicas = ProductosSource.productos
            .map { it.categoria }
            .distinct()
            .toMutableList()

        // Inserta la opción "todos" al principio para que siempre sea el primer filtro.
        categoriasUnicas.add(0, "todos")
        return categoriasUnicas
    }
}