package com.example.milsaboresapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.viewmodel.ConfiguracionViewModel

// Dark theme
private val DarkColorScheme = darkColorScheme(
    primary = ColorPrimary,
    secondary = ColorPrimaryDark,
    tertiary = ColorLight,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onTertiary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText
)

// Light theme
private val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    secondary = ColorPrimaryDark,
    tertiary = ColorLight,
    background = ColorBackground,
    surface = ColorLight,
    onPrimary = ColorText,
    onSecondary = ColorBackground,
    onTertiary = ColorText,
    onBackground = ColorText,
    onSurface = ColorText
)

@Composable
fun MilSaboresAppTheme(
    dynamicColor: Boolean = true,
    // 👈 AÑADIMOS EL VIEWMODEL COMO PARÁMETRO CON VALOR POR DEFECTO
    configViewModel: ConfiguracionViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    // 1. OBTENER EL ESTADO PERSISTENTE
    val modoOscuroActivo by configViewModel.modoOscuroActivo.collectAsState()

    // 2. DECIDIR QUÉ MODO DE TEMA USAR
    // Si modoOscuroActivo es true o false, usamos ese valor.
    // Si es null (aún cargando de DataStore), usamos la configuración del sistema.
    val useDarkTheme = when (modoOscuroActivo) {
        true -> true
        false -> false
        null -> isSystemInDarkTheme()
    }

    // 3. SELECCIÓN DEL ESQUEMA DE COLOR
    val colorScheme = when {
        // Opción de Material Design 3: Usar color dinámico en Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Opción: Usar tu esquema oscuro personalizado
        useDarkTheme -> DarkColorScheme
        // Opción: Usar tu esquema claro personalizado
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, // Aplicar el esquema seleccionado
        typography = Typography,
        content = content
    )
}