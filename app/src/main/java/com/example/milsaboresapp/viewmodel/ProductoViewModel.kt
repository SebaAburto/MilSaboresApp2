package com.example.milsaboresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.repository.ProductRepository
import com.example.milsaboresapp.model.Producto
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    // Estado para la categoría actualmente seleccionada (se inicializa en "todos")
    private val _categoriaSeleccionada = MutableStateFlow("todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    // Estado para almacenar la lista de todas las categorías (incluyendo "todos")
    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias

    // Flujo que escucha los cambios en 'categoriaSeleccionada' y trae la lista de productos filtrada
    val productos: StateFlow<List<Producto>> = _categoriaSeleccionada
        .mapLatest { categoria ->

            repository.getProductosPorCategoria(categoria)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadCategorias()
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            _categorias.value = repository.getCategorias()
        }
    }

    fun setCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }
}