package com.example.milsaboresapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.min

object ImageUtil {

    // Convertir URI (Galería) -> TEXTO BASE64 (Para guardar en BD)
    fun uriToBase64(context: Context, uri: Uri): String? {
        try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // Redimensionar a 800px para no saturar la BD
            val maxDimension = 800
            val scale = min(
                maxDimension.toDouble() / originalBitmap.width,
                maxDimension.toDouble() / originalBitmap.height
            )

            val newWidth = if (scale < 1) (originalBitmap.width * scale).toInt() else originalBitmap.width
            val newHeight = if (scale < 1) (originalBitmap.height * scale).toInt() else originalBitmap.height

            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

            // Comprimir
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()

            val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            return "data:image/jpeg;base64,$base64String"

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // Convertir TEXTO BASE64 (De BD) -> BITMAP (Para mostrar en pantalla)
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val cleanString = if (base64String.contains(",")) {
                base64String.split(",")[1]
            } else {
                base64String
            }
            val decodedBytes = Base64.decode(cleanString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}