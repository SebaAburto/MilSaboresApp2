package com.example.milsaboresapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Inicializa DataStore como un singleton accesible mediante Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracion_app")

class ConfiguracionDataStore(private val context: Context) {

    // Clave para guardar el valor booleano
    private val MODO_OSCURO = booleanPreferencesKey("modo_oscuro_activado")

    /**
     * Guarda el estado booleano de la preferencia.
     */
    suspend fun guardarModoOscuro(valor: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = valor
        }
    }

    /**
     * Expone un Flow que emite el estado actual del modo oscuro.
     */
    fun obtenerModoOscuro(): Flow<Boolean?> =
        context.dataStore.data.map { prefs ->
            prefs[MODO_OSCURO]
        }
}