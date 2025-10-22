package com.example.milsaboresapp.model

data class Login (
    val correo: String = "",
    val clave: String = "",
    val errores: UsuarioLoginErrores = UsuarioLoginErrores ()
)

data class UsuarioLoginErrores(
    val correo: String? = null,
    val clave: String? = null
)