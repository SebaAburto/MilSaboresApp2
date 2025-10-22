package com.example.milsaboresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.model.ItemCarrito
import com.example.milsaboresapp.repository.CarritoRepository
import com.example.milsaboresapp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.milsaboresapp.model.Producto // Asegúrate de tener esta importación

/**
 * Data class que representa el estado de la UI para la pantalla de detalle.
 * Está consolidada aquí.
 */
data class ProductoDetalleUiState(
    val producto: Producto? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * VIEWMODEL: Contiene la lógica para cargar un producto, gestionar su cantidad
 * y añadirlo al carrito.
 */
class ProductoDetalleViewModel(
    private val sku: String,
    private val productosRepo: ProductRepository,
    private val carritoRepo: CarritoRepository
) : ViewModel() {

    // Estado del producto y carga
    private val _uiState = MutableStateFlow(ProductoDetalleUiState())
    val uiState: StateFlow<ProductoDetalleUiState> = _uiState

    // Cantidad seleccionada
    private val _cantidad = MutableStateFlow(1)
    val cantidad: StateFlow<Int> = _cantidad

    init {
        cargarProducto()
    }

    private fun cargarProducto() {
        viewModelScope.launch {
            val producto = productosRepo.obtenerProductoPorSku(sku)

            // Usamos la corrección de tipos para evitar el error 'Any'
            _uiState.update { currentState: ProductoDetalleUiState ->
                if (producto != null) {
                    currentState.copy(producto = producto, isLoading = false, error = null)
                } else {
                    currentState.copy(isLoading = false, error = "Producto no encontrado.")
                }
            }
        }
    }

    // --- Control de Cantidad ---

    fun incrementarCantidad() {
        _cantidad.update { it + 1 }
    }

    fun decrementarCantidad() {
        if (_cantidad.value > 1) {
            _cantidad.update { it - 1 }
        }
    }

    // --- Lógica del Carrito ---

    fun agregarACarrito() {
        val producto = uiState.value.producto
        val cantidadActual = cantidad.value

        if (producto != null && cantidadActual > 0) {
            val itemCarrito = ItemCarrito(
                sku = producto.sku,
                nombre = producto.nombre,
                precio = producto.precio,
                cantidad = cantidadActual
            )
            carritoRepo.agregar(itemCarrito)

            _cantidad.value = 1
        }
    }
}