package com.example.milsaboresapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.data.local.ConfiguracionDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConfiguracionViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = ConfiguracionDataStore(application)

    // Estado observado por la UI: null (cargando), true/false (estado actual)
    private val _modoOscuroActivo = MutableStateFlow<Boolean?>(null)
    val modoOscuroActivo: StateFlow<Boolean?> = _modoOscuroActivo

    init {
        cargarEstadoInicial()
    }

    private fun cargarEstadoInicial() {
        viewModelScope.launch {
            // Espera el primer valor de DataStore (persistencia)
            // Usa 'false' por defecto si nunca se ha guardado nada
            _modoOscuroActivo.value = dataStore.obtenerModoOscuro().first() ?: false
        }
    }

    /**
     * Alterna el estado del modo oscuro, persiste el cambio y actualiza la UI.
     */
    fun alternarModoOscuro() {
        viewModelScope.launch {
            val nuevoValor = !(_modoOscuroActivo.value ?: false)

            // 1. Persistir el nuevo valor en DataStore
            dataStore.guardarModoOscuro(nuevoValor)

            // 2. Actualizar el StateFlow para recompone la UI
            _modoOscuroActivo.value = nuevoValor
        }
    }
}