# 🍰 MilSaboresApp - Sistema Full Stack de Gestión de Inventario

**MilSaboresApp** es una solución tecnológica integral compuesta por una aplicación móvil nativa (Android) y un microservicio backend (Spring Boot). El sistema permite la gestión en tiempo real del inventario de una pastelería, sincronizando datos en la nube y ofreciedo una experiencia fluida tanto para el administrador como para el cliente.

El proyecto implementa una arquitectura distribuida moderna, utilizando **API REST**, persistencia en **NoSQL** y patrones de diseño avanzados.

---

## ✨ Funcionalidades Clave

### 📱 Cliente Android (Frontend)
* **Gestión de Productos (CRUD):** El administrador puede crear, editar y eliminar productos.
* **Manejo de Imágenes:** Selección de fotos desde **Cámara o Galería**, con conversión automática a Base64 para su transmisión.
* **Catálogo Dinámico:** Filtrado de productos por categorías (las cuales se autogestionan según el inventario).
* **Control de Stock:** Indicadores visuales de "Stock Bajo" y bloqueo de productos "Agotados".
* **Carrito de Compras:** Lógica local para agregar productos y calcular totales.

### ⚙️ Backend & Nube
* **API RESTful:** Microservicio en Spring Boot que expone endpoints para la gestión de datos.
* **Persistencia Cloud:** Conexión segura con **Google Firebase Firestore** mediante Firebase Admin SDK.
* **Lógica de Negocio:** Validación de datos, gestión automática de categorías vacías y manejo de errores.

---

## 🛠️ Tecnologías y Arquitectura

El proyecto sigue el patrón **MVVM** (Model-View-ViewModel) en el cliente y una arquitectura de capas (Controller-Service-Repository) en el backend.

| Categoría | Tecnologías | Descripción |
| :--- | :--- | :--- |
| **Android UI** | **Jetpack Compose** | Interfaz de usuario moderna y declarativa. |
| **Lenguaje** | **Kotlin** | Desarrollo nativo Android. |
| **Red & API** | **Retrofit + Gson** | Cliente HTTP y serialización de datos JSON. |
| **Imágenes** | **Coil** | Carga y caché eficiente de imágenes asíncronas. |
| **Asincronía** | **Coroutines & Flow** | Manejo de hilos en segundo plano y estados reactivos. |
| **Backend** | **Java & Spring Boot** | Microservicio REST. |
| **Base de Datos** | **Firebase Firestore** | Base de datos NoSQL en la nube. |
| **Infraestructura** | **Ngrok** | Tunelizado seguro para exponer el localhost a internet. |

### 🧪 Calidad y Testing
El proyecto cuenta con una sólida cobertura de pruebas unitarias (**>80% de cobertura**) en la capa de lógica de negocio del cliente.
* **JUnit 4:** Framework base de pruebas.
* **MockK:** Simulación de dependencias (API, Repositorios).
* **Turbine:** Pruebas de flujos reactivos (`StateFlow`).
* **Kotlinx-coroutines-test:** Control de tiempo y despachadores en tests.

---

## 🏗️ Flujo de Datos

La comunicación sigue el siguiente ciclo de vida:

`Android (ViewModel)` ↔ `Retrofit (OkHttp)` ↔ `Túnel Ngrok` ↔ `Spring Boot (Controller)` ↔ `Firebase Firestore`

---

## ▶️ Guía de Ejecución (Paso a Paso)

Para probar el sistema completo, se requiere ejecutar tanto el Backend como el Cliente.

### 1. Preparar el Backend
1.  Abre el proyecto del microservicio en tu IDE de Java (IntelliJ/Eclipse).
2.  Asegúrate de tener el archivo `serviceAccountKey.json` de Firebase en la carpeta `src/main/resources`.
3.  Ejecuta la clase principal `MicroservicioInventarioPmsApplication`.
4.  Verifica que corra en el puerto `8080`.

### 2. Exponer con Ngrok
Abre una terminal y ejecuta:
`ngrok http 8080`

Copia la dirección HTTPS que te genera (ej: `https://a1b2-c3d4.ngrok-free.dev`).

### 3. Configurar Android
Abre el proyecto Android en Android Studio.

Ve al archivo `network/RetrofitClient.kt.`

Actualiza la variable BASE_URL con la dirección de Ngrok del paso anterior.

`private const val BASE_URL = "[https://tu-url-de-ngrok.ngrok-free.dev/](https://tu-url-de-ngrok.ngrok-free.dev/)"`

### 4. Ejecutar la App
Selecciona tu emulador o dispositivo físico conectado.

Presiona Run ▶️.

¡Y listo!

---

### 👨‍💻 Autores
Sebastian Aburto

Iván Santander
