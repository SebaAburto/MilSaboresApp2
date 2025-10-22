package com.example.milsaboresapp.ui.screens

import com.example.milsaboresapp.viewmodel.LoginViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.milsaboresapp.ui.theme.ColorLight
@Composable
fun LoginScreen(navController: NavController? = null , viewModel: LoginViewModel = viewModel()) {
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
        Button(
            onClick = {
                if (viewModel.validarLogin()) {
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)

            ) {
            Text("Iniciar Sesion")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen ()
}