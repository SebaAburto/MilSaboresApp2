package com.example.milsaboresapp.di

import com.example.milsaboresapp.repository.CarritoRepository
import com.example.milsaboresapp.repository.CarritoRepositoryImpl


object AppGraph {
    // carrito unico
    val cartRepo: CarritoRepository = CarritoRepositoryImpl()
}
