package com.example.milsaboresapp.ui.screens
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.ui.theme.ColorLight
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NosotrosScreen() {
    val context = LocalContext.current
    // Coordenadas aproximadas de Álvarez 2366, Viña del Mar (Ajustar si es necesario)
    val localizacionTienda = LatLng(-33.03332039729435, -71.53317337470516)
    // Estado inicial de la cámara del mapa (Zoom 15f es nivel calle)
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(localizacionTienda, 15f)}


    Column(
        modifier = Modifier
            .background(ColorLight)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Título principal
        Text(
            text = "Sobre Nosotros",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto de presentación
        Text(
            text = """
                Pastelería 1000 Sabores celebra su 50 aniversario como un referente en la repostería chilena. 
                Somos reconocidos por nuestra participación en el Récord Guinness de 1995, cuando colaboramos 
                en la creación de la torta más grande del mundo. 

                Hoy, renovamos nuestra experiencia online para llevar nuestras delicias directamente a tu hogar, 
                combinando tradición con innovación.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Misión
        Text(
            text = "Misión",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.align(Alignment.Start),
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = """
                Ofrecer una experiencia dulce y memorable a nuestros clientes, 
                proporcionando tortas y productos de repostería de alta calidad 
                para todas las ocasiones, mientras celebramos nuestras raíces 
                históricas y fomentamos la creatividad en la repostería.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Visión
        Text(
            text = "Visión",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.align(Alignment.Start),
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = """
                Convertirnos en la tienda online líder de productos de repostería en Chile, 
                conocida por nuestra innovación, calidad y el impacto positivo en la comunidad, 
                especialmente en la formación de nuevos talentos en gastronomía.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Contacto",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.align(Alignment.Start),
            fontSize = 28.sp
        )

        //info contacto
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "✉️ Correo: contacto@1000sabores.cl",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "📞 Teléfono: +56 9 1234 5678",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:+56912345678")
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "📍 Ubicación: Álvarez 2366, Chorrillos",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // Marcador rojo estándar en la ubicación
                    Marker(
                        state = MarkerState(position = localizacionTienda),
                        title = "1000 Sabores",
                        snippet = "Ven a visitarnos!"
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}
