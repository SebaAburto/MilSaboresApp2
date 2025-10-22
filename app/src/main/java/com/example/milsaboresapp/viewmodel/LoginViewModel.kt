package com.example.milsaboresapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.milsaboresapp.model.Login
import com.example.milsaboresapp.model.UsuarioLoginErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _estado = MutableStateFlow(Login())

    val estado: StateFlow<Login> = _estado

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }
    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun validarLogin(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioLoginErrores(
            correo = if (estadoActual.correo.isBlank()) "Campo requerido" else null ,
            clave = if (estadoActual.clave.isBlank()) "Campo requerido" else null
        )

        val hayErrores = listOfNotNull(
             errores.correo, errores.clave
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }
}