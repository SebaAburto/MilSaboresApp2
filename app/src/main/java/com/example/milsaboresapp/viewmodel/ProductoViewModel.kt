package com.example.milsaboresapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.repository.ProductRepository
import com.example.milsaboresapp.util.ImageUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // ESTADOS PARA LA LISTA (FILTROS)
    private val _categoriaSeleccionada = MutableStateFlow("todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias

    // Trigger para recargar la lista manualmente después de un cambio
    private val _reloadTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    // Combinar la categoría + el trigger para obtener la lista actualizada
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

    // verificar si las operaciones (guardar/editar) fueron exitosas.
    private val _operacionExitosa = MutableStateFlow(false)
    val operacionExitosa: StateFlow<Boolean> = _operacionExitosa

    // El producto obtenido para editar
    private val _productoObtenido = MutableStateFlow<Producto?>(null)
    val productoObtenido: StateFlow<Producto?> = _productoObtenido

    // Mensajes de error para mostrar en Toast (pequeña alerta de android)
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

    // FUNCIONES CRUD

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

    // Funciones básicas (inutilizadas con la nueva version de agregar con foto)
    fun addProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.addProducto(producto)
                _reloadTrigger.emit(Unit)
                loadCategorias()
                _operacionExitosa.value = true
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
                _operacionExitosa.value = true
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
            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar: ${e.message}"
            }
        }
    }

    // Nueva función, ya contiene logica que decide si editar o crear,
    // llamar directamente al repositorio y agregar con imagen.

    fun guardarProductoConImagen(context: Context, producto: Producto, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                // imagen que ya tenía el producto
                var stringImagenFinal = producto.imageUrl

                // si el usuario selecciona una NUEVA foto, la convierte a base64
                if (imageUri != null && imageUri.toString().contains("content://")) {

                    val base64Image = ImageUtil.uriToBase64(context, imageUri)

                    if (base64Image != null) {
                        stringImagenFinal = base64Image
                    }
                }

                // Actualizar el objeto
                val productoParaGuardar = producto.copy(imageUrl = stringImagenFinal)

                // Decidir si es CREAR o EDITAR
                if (producto.id == null) {
                    repository.addProducto(productoParaGuardar)
                } else {
                    repository.updateProducto(productoParaGuardar)
                }

                // Notificar éxito en la operación y recargar las listas
                _reloadTrigger.emit(Unit)
                loadCategorias()
                _operacionExitosa.value = true

            } catch (e: Exception) {
                _mensajeError.value = "Error al procesar imagen: ${e.message}"
            }
        }
    }
}