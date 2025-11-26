package com.example.milsaboresapp.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


//FileUtil: utiliza FileProvider para generar un URI de la imagen, conformado por el prefijo y sufijo.

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
        "${context.packageName}.fileprovider",
        tempFile
    )
}