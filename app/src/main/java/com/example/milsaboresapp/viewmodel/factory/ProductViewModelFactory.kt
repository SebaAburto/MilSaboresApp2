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
        //verifica que sea la clase correcta
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            //crea la instancia viewmodel inyectando dependencia
            return ProductViewModel(repository) as T
        } //en caso de error, notifica.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}