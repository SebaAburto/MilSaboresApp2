package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.milsaboresapp.model.Formulario
import com.example.milsaboresapp.ui.theme.ColorLight
import com.example.milsaboresapp.viewmodel.FormularioViewModel

@Composable
fun FormularioScreen(navController: NavController? = null , viewModel: FormularioViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val estado by viewModel.estado.collectAsState()



    Column(
        Modifier
            .fillMaxSize()
            .background(ColorLight)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        val coloresFondo = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            disabledContainerColor = Color.White
        )

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = coloresFondo
        )
        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text("Correo") },
            isError = estado.errores.correo != null,
            supportingText = {
                estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = coloresFondo
        )
        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = { Text("Contraseña") },
            isError = estado.errores.clave != null,
            supportingText = {
                estado.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = coloresFondo
        )
        OutlinedTextField(
            value = estado.repetirClave,
            onValueChange = viewModel::onRepetirClaveChange,
            label = { Text("Repetir contraseña") },
            isError = estado.errores.repetirClave != null,
            supportingText = {
                estado.errores.repetirClave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = coloresFondo
        )
        OutlinedTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChange,
            label = { Text("Direccion") },
            isError = estado.errores.direccion != null,
            supportingText = {
                estado.errores.direccion?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = coloresFondo
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,

                    uncheckedColor = Color.Gray,

                    // (Opcional) Color de la marca de verificación (check)
                    checkmarkColor = Color.White
                )
            )
            Text("Acepto los términos y condiciones")

        }
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = estado.aceptaTerminos // solo se habilita si acepta
        ) {
            Text("Crear cuenta")
        }

    }
}

@Composable
fun ResumenScreen(viewModel: FormularioViewModel) {
    val estado by viewModel.estado.collectAsState()
    Column (Modifier.padding(16.dp)){
        Text("Resumen del registro", style = MaterialTheme.typography.headlineMedium)
        Text("Nombre: ${estado.nombre}")
        Text("Correo: ${estado.correo}")
        Text("Direccion: ${estado.direccion}")
        Text("Contraseña: ${"*".repeat(estado.clave.length)}")
        Text("¿Terminos aceptados? ${if(estado.aceptaTerminos) "Si" else "No"}")
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioPreview() {
    // NO use viewModel() aquí — crea una instancia directa del ViewModel para el preview
    FormularioScreen ()
}