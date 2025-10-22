package com.example.milsaboresapp.data.remote

import com.example.milsaboresapp.model.Producto

object ProductosSource {

        val productos = listOf(
                Producto(sku = "TC001", nombre = "Torta Cuadrada de Chocolate", descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.", categoria = "normal", precio = 45000.0, imageUrl = "tc001", destacado = true),
                Producto(sku = "TC002", nombre = "Torta Cuadrada de Frutas", descripcion = "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.", categoria = "normal", precio = 50000.0, imageUrl = "tc002", destacado = true),
                Producto(sku = "TT001", nombre = "Torta Circular de Vainilla", descripcion = "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.", categoria = "normal", precio = 40000.0, imageUrl = "tt001", destacado = true),
                Producto(sku = "TT002", nombre = "Torta Circular de Manjar", descripcion = "Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.", categoria = "normal", precio = 42000.0, imageUrl = "tt002", destacado = true),
                Producto(sku = "PI001", nombre = "Mousse de Chocolate", descripcion = "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.", categoria = "postre", precio = 5000.0, imageUrl = "pi001", destacado = false),
                Producto(sku = "PI002", nombre = "Tiramisú Clásico", descripcion = "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.", categoria = "postre", precio = 5500.0, imageUrl = "pi002", destacado = false),
                Producto(sku = "PSA001", nombre = "Torta Sin Azúcar de Naranja", descripcion = "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.", categoria = "sin-azucar", precio = 48000.0, imageUrl = "psa001", destacado = true),
                Producto(sku = "PSA002", nombre = "Cheesecake Sin Azúcar", descripcion = "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.", categoria = "sin-azucar", precio = 47000.0, imageUrl = "psa002", destacado = false),
                Producto(sku = "PT001", nombre = "Empanada de Manzana", descripcion = "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.", categoria = "normal", precio = 3000.0, imageUrl = "pt001", destacado = false),
                Producto(sku = "PT002", nombre = "Tarta de Santiago", descripcion = "Tradicional tarta española hecha con almendras, azúcar y huevos, una delicia para los amantes de los postres clásicos.", categoria = "normal", precio = 6000.0, imageUrl = "pt002", destacado = false),
                Producto(sku = "PG001", nombre = "Brownie Sin Gluten", descripcion = "Rico y denso, perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.", categoria = "sin-gluten", precio = 4000.0, imageUrl = "pg001", destacado = true),
                Producto(sku = "PG002", nombre = "Pan Sin Gluten", descripcion = "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.", categoria = "sin-gluten", precio = 3500.0, imageUrl = "pg002", destacado = false),
                Producto(sku = "PV001", nombre = "Torta Vegana de Chocolate", descripcion = "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.", categoria = "vegano", precio = 50000.0, imageUrl = "pv001", destacado = true),
                Producto(sku = "PV002", nombre = "Galletas Veganas de Avena", descripcion = "Crujientes y sabrosas, una excelente opción para un snack saludable y vegano.", categoria = "vegano", precio = 4500.0, imageUrl = "pv002", destacado = false),
                Producto(sku = "TE001", nombre = "Torta Especial de Cumpleaños", descripcion = "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.", categoria = "especial", precio = 55000.0, imageUrl = "te001", destacado = true),
                Producto(sku = "TE002", nombre = "Torta Especial de Boda", descripcion = "Elegante y deliciosa, diseñada para ser el centro de atención en cualquier boda.", categoria = "especial", precio = 60000.0, imageUrl = "te002", destacado = false)
        )
}