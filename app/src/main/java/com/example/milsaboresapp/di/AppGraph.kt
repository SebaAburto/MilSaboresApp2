package com.example.milsaboresapp.di

import com.example.milsaboresapp.repository.CarritoRepository
import com.example.milsaboresapp.repository.CarritoRepositoryImpl
import com.example.milsaboresapp.repository.ProductRepository
import com.example.milsaboresapp.repository.ProductRepositoryImpl // 🚨 Asegúrate de tener esta importación

object AppGraph {

    val cartRepo: CarritoRepository by lazy {
        CarritoRepositoryImpl()
    }

    val productosRepo: ProductRepository by lazy {

        ProductRepositoryImpl()
    }
}