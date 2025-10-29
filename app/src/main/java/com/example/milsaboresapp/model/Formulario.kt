package com.example.milsaboresapp.model
import android.net.Uri
data class Formulario(
    val nombre: String ="",
    val correo: String = "",
    val clave: String = "",
    val repetirClave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val fotoPerfilUri: Uri? = null,
    val errores: UsuarioErrores = UsuarioErrores ()
)

data class UsuarioErrores (
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    var repetirClave: String? = null,
    val direccion: String? = null
)