package com.example.milsaboresapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.milsaboresapp.repository.ProductRepository
import com.example.milsaboresapp.viewmodel.ProductViewModel

//FACTORY: Permite al sistema de Compose/Android instanciar el ViewModel
//inyectando las dependencias (Repositorios) y el argumento de navegación (SKU).

class ProductViewModelFactory(
    private val repository: ProductRepository // Recibe la dependencia ProductRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {

            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}