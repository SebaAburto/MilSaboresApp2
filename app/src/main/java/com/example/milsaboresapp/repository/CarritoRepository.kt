package com.example.milsaboresapp.repository

import com.example.milsaboresapp.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface CarritoRepository {
    val items: StateFlow<List<ItemCarrito>>

    fun agregar(item: ItemCarrito)
    fun incrementar(sku: String)
    fun decrementar(sku: String)
    fun eliminar(sku: String)
    fun limpiar()
}


class CarritoRepositoryImpl : CarritoRepository {
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    override val items: StateFlow<List<ItemCarrito>> = _items

    //Funcion para agregar al item, si el item ya está agregado, aumenta la cantidad seleccionada.
    override fun agregar(item: ItemCarrito) = _items.update { list ->
        val existingItem = list.find { it.sku == item.sku }

        if (existingItem != null) {
            list.map {
                if (it.sku == item.sku) {
                    it.copy(cantidad = it.cantidad + item.cantidad)
                } else {
                    it
                }
            }
        } else {
            //agrega naturalmente si no existe
            list + item
        }
    }

    override fun incrementar(sku: String) = _items.update { list ->
        list.map {
            if (it.sku == sku) it.copy(cantidad = it.cantidad + 1) else it
        }
    }

    //funcion de decrementar la cantidad, si se llega a 0, el item se elimina
    override fun decrementar(sku: String) = _items.update { list ->
        val targetItem = list.find { it.sku == sku }

        if (targetItem != null) {
            val nuevaCantidad = targetItem.cantidad - 1
            if (nuevaCantidad <= 0) {

                list.filterNot { it.sku == sku }
            } else {

                list.map {
                    if (it.sku == sku) it.copy(cantidad = nuevaCantidad) else it
                }
            }
        } else {
            list
        }
    }

    override fun eliminar(sku: String) = _items.update { list ->
        list.filterNot { it.sku == sku }
    }

    override fun limpiar() {
        _items.value = emptyList()
    }
}