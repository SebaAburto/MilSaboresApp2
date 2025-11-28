package com.example.milsaboresapp.viewmodel

import android.content.Context // 👈 NECESARIO
import android.net.Uri         // 👈 NECESARIO
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.repository.ProductRepository
import com.example.milsaboresapp.util.ImageUtil // 👈 TU NUEVA UTILIDAD
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

    // --- 3. FUNCIONES CRUD ---

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

    // Funciones básicas (usadas internamente o para operaciones sin foto nueva)
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

    // --- 4. NUEVA FUNCIÓN MÁGICA (ESTA ES LA QUE FALTABA) ---
    // Esta función orquesta todo: Conversión de imagen -> Guardado
    fun guardarProductoConImagen(context: Context, producto: Producto, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                // 1. Empezamos con la imagen que ya tenía el producto (logo, url vieja o base64 viejo)
                var stringImagenFinal = producto.imageUrl

                // 2. Si el usuario seleccionó una foto NUEVA (de galería o cámara)
                if (imageUri != null && imageUri.toString().contains("content://")) {

                    // Convertimos la URI a Texto Base64 usando tu nueva utilidad
                    val base64Image = ImageUtil.uriToBase64(context, imageUri)

                    if (base64Image != null) {
                        stringImagenFinal = base64Image
                    }
                }

                // 3. Actualizamos el objeto con la imagen procesada
                val productoParaGuardar = producto.copy(imageUrl = stringImagenFinal)

                // 4. Decidimos si es CREAR o EDITAR
                if (producto.id == null) {
                    repository.addProducto(productoParaGuardar)
                } else {
                    repository.updateProducto(productoParaGuardar)
                }

                // 5. Notificamos éxito y recargamos listas
                _reloadTrigger.emit(Unit)
                loadCategorias()
                _operacionExitosa.value = true

            } catch (e: Exception) {
                _mensajeError.value = "Error al procesar imagen: ${e.message}"
            }
        }
    }
}