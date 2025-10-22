package com.example.milsaboresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.milsaboresapp.repository.CarritoRepository
import com.example.milsaboresapp.repository.ProductRepository

/**
 * FACTORY: Permite al sistema de Compose/Android instanciar el ViewModel
 * inyectando las dependencias (Repositorios) y el argumento de navegación (SKU).
 */
class ProductoDetalleViewModelFactory(
    private val sku: String,
    private val productosRepo: ProductRepository,
    private val carritoRepo: CarritoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoDetalleViewModel::class.java)) {
            // Se asume que ProductoDetalleViewModel está importable desde aquí
            return ProductoDetalleViewModel(sku, productosRepo, carritoRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}