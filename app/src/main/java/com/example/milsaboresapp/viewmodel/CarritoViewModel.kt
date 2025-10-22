package com.example.milsaboresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.model.ItemCarrito
import com.example.milsaboresapp.repository.CarritoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class CarritoUiState(
    val items: List<ItemCarrito> = emptyList(),
    val total: Double = 0.0,
    val totalCLP: String = "$0",
    val count: Int = 0
)

class CarritoViewModel (
    private val repo: CarritoRepository
) : ViewModel() {

    private val currencyFormatter: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("es", "CL"))


    val ui: StateFlow<CarritoUiState> = repo.items
        .map { list ->
            val tot = list.sumOf { it.precio * it.cantidad }

            CarritoUiState(
                items = list,
                total = tot,
                // APLICACIÓN DEL FORMATO:
                totalCLP = currencyFormatter.format(tot),
                count = list.sumOf { it.cantidad }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CarritoUiState()
        )

    fun agregar(item: ItemCarrito) = viewModelScope.launch { repo.agregar(item) }
    fun incrementar(sku: String) = viewModelScope.launch { repo.incrementar(sku) }
    fun decrementar(sku: String) = viewModelScope.launch { repo.decrementar(sku) }
    fun eliminar(sku: String) = viewModelScope.launch { repo.eliminar(sku) }
    fun limpiar() = viewModelScope.launch { repo.limpiar() }
}