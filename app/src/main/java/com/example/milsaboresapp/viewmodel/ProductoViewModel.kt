package com.example.milsaboresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // --- 1. ESTADOS PARA LA LISTA (FILTROS) ---
    private val _categoriaSeleccionada = MutableStateFlow("todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias

    // Gatillo para recargar la lista manualmente después de un cambio
    private val _reloadTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    // Combinamos la categoría + el gatillo para obtener la lista actualizada
    val productos: StateFlow<List<Producto>> = combine(_categoriaSeleccionada, _reloadTrigger) { categoria, _ ->
        if (categoria == "todos") {
            repository.getProductos()
        } else {
            repository.getProductosPorCategoria(categoria)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- 2. NUEVOS ESTADOS FALTANTES (Para el Formulario) ---

    // ¿La operación (Guardar/Editar) terminó bien?
    private val _operacionExitosa = MutableStateFlow(false)
    val operacionExitosa: StateFlow<Boolean> = _operacionExitosa

    // El producto que descargamos para editar
    private val _productoObtenido = MutableStateFlow<Producto?>(null)
    val productoObtenido: StateFlow<Producto?> = _productoObtenido

    // Mensajes de error para mostrar en Toast
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError

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

    // Limpiar estados para que al volver a entrar no se cierre sola la pantalla
    fun resetOperacion() {
        _operacionExitosa.value = false
        _productoObtenido.value = null
        _mensajeError.value = null
    }

    // --- 3. FUNCIONES CRUD (Con manejo de errores y estados) ---

    // Obtener un producto por ID (Para llenar el formulario al editar)
    fun obtenerProductoPorId(id: String) {
        viewModelScope.launch {
            try {
                val producto = repository.getProductoPorId(id)
                _productoObtenido.value = producto
            } catch (e: Exception) {
                _mensajeError.value = "Error cargando producto: ${e.message}"
            }
        }
    }

    fun addProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.addProducto(producto)
                _reloadTrigger.emit(Unit) // Recargar lista
                loadCategorias()          // Recargar categorías
                _operacionExitosa.value = true // ¡Avisar a la pantalla que termine!
            } catch (e: Exception) {
                _mensajeError.value = "Error al guardar: ${e.message}"
            }
        }
    }

    fun updateProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.updateProducto(producto)
                _reloadTrigger.emit(Unit)
                loadCategorias()
                _operacionExitosa.value = true // ¡Avisar a la pantalla que termine!
            } catch (e: Exception) {
                _mensajeError.value = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun deleteProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.deleteProducto(producto)
                _reloadTrigger.emit(Unit)
                loadCategorias()
                // Aquí no ponemos operacionExitosa = true porque delete ocurre en la misma pantalla
            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar: ${e.message}"
            }
        }
    }
}