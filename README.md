# 📱 MilSaboresApp 🍰

## ¿Qué hace la aplicación?

MilSaboresApp es una aplicación móvil desarrollada en **Kotlin y Jetpack Compose** que simula una tienda de pastelería en línea. Permite a los usuarios **registrarse, iniciar sesión, explorar el catálogo de productos** y gestionar un **carrito de compras** con funcionalidades completas de adición, incremento, decremento y eliminación de ítems, culminando en un proceso de pago simplificado.

El objetivo principal es ofrecer una experiencia de compra móvil fluida e intuitiva.

---

## ▶️ ¿Cómo se ejecuta?

1. Clona el repositorio:

    ```bash
    git clone [https://github.com/SebaAburto/MilSaboresApp2.git](https://github.com/SebaAburto/MilSaboresApp2.git)
    ```
2. Abre el proyecto en **Android Studio**.
3. Asegúrate de tener configurado un emulador o dispositivo físico con Android 8.0 (API 26) o superior.
4. Presiona **Run ▶️** para compilar y ejecutar la aplicación.

---

## 🛠️ Tecnologías y Arquitectura

Este proyecto utiliza el stack moderno de Android, implementando el patrón MVVM y el manejo de estado reactivo.

| Categoría | Tecnología Clave | Uso Específico |
| :--- | :--- | :--- |
| **Lenguaje** | **Kotlin** | Lenguaje principal de desarrollo. |
| **Interfaz (UI)** | **Jetpack Compose** | Framework declarativo para la construcción de la interfaz. |
| **Arquitectura** | **MVVM** | Patrón para la separación de responsabilidades (Model-View-ViewModel). |
| **Estado y Asincronía** | **Kotlin Flow / Coroutines** | Gestión reactiva del estado de la UI (`StateFlow`) y manejo de operaciones asíncronas. |
| **Datos** | **Repository Pattern** | Abstracción de la capa de datos (catálogo y carrito). |
| **Navegación** | **Compose Navigation** | Gestión del flujo de pantallas de la aplicación. |

---

## 👨‍💻 Autores

**Sebastian Aburto**
**Iván Santander**
