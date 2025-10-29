package com.example.milsaboresapp.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


//FileUtil crea una URI temporal y única usando FileProvider para que la cámara guarde la foto.

fun createImageUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "temp_image", // prefijo del archivo
        ".jpg",       // sufijo del archivo
        context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
    ).apply {
        createNewFile()
    }

    // Retorna la URI del FileProvider
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider", // Debe coincidir con el 'authorities' del Manifest
        tempFile
    )
}