package com.example.milsaboresapp.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.milsaboresapp.util.createImageUri

@Composable
fun ImageSelector(
    imageModel: Any?, // Acepta Uri, Bitmap o Int
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (imageModel != null) {
                // Coil: si le pasas un Bitmap (decodificado del Base64), lo muestra.
                Image(
                    painter = rememberAsyncImagePainter(imageModel),
                    contentDescription = "Imagen seleccionada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar Foto",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Toca para añadir foto", color = Color.Gray)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar Imagen") },
            text = { Text("Elige el origen:") },
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