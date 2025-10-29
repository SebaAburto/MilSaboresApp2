package com.example.milsaboresapp.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.milsaboresapp.util.createImageUri // Asegúrate de que esta utilidad esté creada

@Composable
fun ImageSelector(
    currentUri: Uri?,
    onUriSelected: (Uri?) -> Unit // Para enviar la URI al ViewModel
) {
    val context = LocalContext.current
    // Estado temporal para la URI de la cámara.
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // El permiso de almacenamiento cambia según la versión de Android (API 33+)
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // --- LANZADORES DE ACTIVIDAD ---

    // 1. LANZADOR DE GALERÍA
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { resultUri: Uri? ->
        onUriSelected(resultUri)
    }

    // 2. LANZADOR DE CÁMARA
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            onUriSelected(tempImageUri)
        } else {
            tempImageUri = null
        }
    }

    // 3. LANZADOR DE PERMISOS (CÁMARA y GALERÍA)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 🔑 SOLUCIÓN AL ERROR: Creamos una copia local inmutable
            val currentTempUri = tempImageUri

            // Usamos la copia local para el Smart Cast
            if (currentTempUri != null) {
                cameraLauncher.launch(currentTempUri) // Lanzar cámara
            } else {
                galleryLauncher.launch("image/*") // Lanzar galería
            }
        } else {
            // Permiso denegado
        }
    }

    // --- UI DEL SELECTOR ---

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { showDialog = true }, // Abre el diálogo al hacer click
        contentAlignment = Alignment.Center
    ) {
        if (currentUri != null) {
            Image(
                painter = rememberAsyncImagePainter(currentUri),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Seleccionar Foto",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    // --- DIÁLOGO DE OPCIONES ---

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Añadir Foto") },
            text = { Text("Selecciona el origen de la imagen.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    // Preparamos la URI temporal y solicitamos permiso de CÁMARA
                    tempImageUri = createImageUri(context)
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    tempImageUri = null
                    // Verificamos si ya tenemos el permiso de Galería/Almacenamiento
                    if (ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED) {
                        galleryLauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(storagePermission) // Solicitamos permiso de Galería
                    }
                }) {
                    Text("Galería")
                }
            }
        )
    }
}