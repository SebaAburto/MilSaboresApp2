package com.example.milsaboresapp.repository

import com.example.milsaboresapp.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// 1. Interfaz (Contrato)
interface CarritoRepository {
    val items: StateFlow<List<ItemCarrito>>

    fun agregar(item: ItemCarrito)
    fun incrementar(sku: String)
    fun decrementar(sku: String)
    fun eliminar(sku: String)
    fun limpiar()
}

// 2. Implementación (Clase Concreta)
class CarritoRepositoryImpl : CarritoRepository {
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    override val items: StateFlow<List<ItemCarrito>> = _items

    // CORRECCIÓN en 'agregar': Si el ítem ya existe, solo se incrementa la cantidad en 1.
    // La cantidad inicial del ítem nuevo (item.cantidad) solo se usa si es nuevo.
    override fun agregar(item: ItemCarrito) = _items.update { list ->
        val existingItem = list.find { it.sku == item.sku }

        if (existingItem != null) {
            // Si existe, incrementa la cantidad del ítem existente en 1 (o la cantidad que se esté agregando)
            list.map {
                if (it.sku == item.sku) {
                    // Usamos item.cantidad del ítem que se quiere agregar
                    it.copy(cantidad = it.cantidad + item.cantidad)
                } else {
                    it
                }
            }
        } else {
            // Si es nuevo, lo agrega tal cual con la cantidad ya definida
            list + item
        }
    }

    override fun incrementar(sku: String) = _items.update { list ->
        list.map {
            if (it.sku == sku) it.copy(cantidad = it.cantidad + 1) else it
        }
    }

    override fun decrementar(sku: String) = _items.update { list ->
        val targetItem = list.find { it.sku == sku }

        if (targetItem != null) {
            val nuevaCantidad = targetItem.cantidad - 1
            if (nuevaCantidad <= 0) {
                // OPTIMIZACIÓN: Si llega a 0, se ELIMINA de la lista.
                list.filterNot { it.sku == sku }
            } else {
                // Si la cantidad sigue siendo > 0, decrementa.
                list.map {
                    if (it.sku == sku) it.copy(cantidad = nuevaCantidad) else it
                }
            }
        } else {
            list // No hay cambios si el ítem no se encuentra (caso borde)
        }
    }

    override fun eliminar(sku: String) = _items.update { list ->
        list.filterNot { it.sku == sku }
    }

    override fun limpiar() {
        _items.value = emptyList()
    }
}