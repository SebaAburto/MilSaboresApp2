package com.example.milsaboresapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.milsaboresapp.repository.ProductRepository // Importa tu interfaz de repositorio
import com.example.milsaboresapp.viewmodel.ProductViewModel
/**
 * FACTORY: Permite al sistema instanciar el ProductViewModel
 * inyectando la dependencia ProductRepository.
 */
class ProductViewModelFactory(
    private val repository: ProductRepository // Recibe la dependencia ProductRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            // Aquí se llama al constructor del ViewModel y se le pasa la dependencia.
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}