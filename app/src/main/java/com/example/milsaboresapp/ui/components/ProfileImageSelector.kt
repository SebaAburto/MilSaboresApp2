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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import com.example.milsaboresapp.util.createImageUri

@Composable
fun ProfileImageSelector(
    currentUri: Uri?,
    onUriSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // Launchers
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { onUriSelected(it) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) onUriSelected(tempImageUri)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            if (tempImageUri != null) cameraLauncher.launch(tempImageUri!!)
            else galleryLauncher.launch("image/*")
        }
    }

    // --- UI CIRCULAR (ESTILO PERFIL) ---
    Box(
        modifier = Modifier
            .size(120.dp) // Tamaño fijo pequeño
            .clip(CircleShape) // Forma circular
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { showDialog = true },
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
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar Foto",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    // Diálogo
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Foto de Perfil") },
            text = { Text("Selecciona el origen:") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    tempImageUri = createImageUri(context)
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) { Text("Cámara") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    tempImageUri = null
                    if (ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED) {
                        galleryLauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(storagePermission)
                    }
                }) { Text("Galería") }
            }
        )
    }
}