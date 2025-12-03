package com.example.milsaboresapp.viewmodel

import com.example.milsaboresapp.model.Producto
import com.example.milsaboresapp.repository.ProductRepository
import com.example.milsaboresapp.util.MainDispatcherRule
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductViewModelTest {

    // Aplicamos la regla para que las corrutinas funcionen en los tests
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mock del repositorio (relaxed = true para que no falle si llamamos métodos no configurados)
    private val repositoryMock = mockk<ProductRepository>(relaxed = true)

    private lateinit var viewModel: ProductViewModel

    private val p1 = Producto(
        id = "1",
        nombre = "Torta A",
        categoria = "Tortas",
        precio = 100.0,
        imageUrl = "",
        sku = "SKU-001",
        descripcion = "Descripción de prueba 1",
        stock = 10,
        enOferta = false,
        precioEnOferta = 0.0,
        destacado = false,
        stockMinimo = 5
    )
    private val p2 = Producto(
        id = "2",
        nombre = "Pie B",
        categoria = "Postres",
        precio = 200.0,
        imageUrl = "",
        sku = "SKU-002",
        descripcion = "Descripción de prueba 2",
        stock = 5,
        enOferta = true,
        precioEnOferta = 180.0,
        destacado = true,
        stockMinimo = 5
    )

    @Test
    fun `Al iniciar, debe cargar las categorias del repositorio`() = runTest {
        // GIVEN: El repositorio devolverá estas categorías
        val catsEsperadas = listOf("todos", "Cat1", "Cat2")
        coEvery { repositoryMock.getCategorias() } returns catsEsperadas

        // WHEN: Iniciamos el ViewModel
        viewModel = ProductViewModel(repositoryMock)

        // THEN: El estado 'categorias' debe tener esos valores
        assertEquals(catsEsperadas, viewModel.categorias.value)
    }

    @Test
    fun `Al cambiar categoria, la lista de productos debe actualizarse`() = runTest {
        // GIVEN: Configuramos las respuestas del repo
        coEvery { repositoryMock.getProductos() } returns listOf(p1, p2)
        coEvery { repositoryMock.getProductosPorCategoria("Cat1") } returns listOf(p1)

        viewModel = ProductViewModel(repositoryMock)

        // WHEN & THEN: Usamos Turbine para observar el flujo
        viewModel.productos.test {
            // 1. Estado inicial vacío
            assertEquals(emptyList<Producto>(), awaitItem())

            // 2. Carga inicial (trae todos)
            assertEquals(listOf(p1, p2), awaitItem())

            // Acción: El usuario cambia el filtro a "Cat1"
            viewModel.setCategoria("Cat1")

            // 3. Debería emitir la lista filtrada (solo p1)
            val listaFiltrada = awaitItem()
            assertEquals(1, listaFiltrada.size)
            assertEquals("A", listaFiltrada[0].nombre)
        }
    }

    @Test
    fun `deleteProducto debe llamar al repositorio y recargar la lista`() = runTest {
        // GIVEN
        viewModel = ProductViewModel(repositoryMock)

        // WHEN
        viewModel.deleteProducto(p1)

        // THEN
        // Verificamos que se ejecutó la orden de borrado en el repo
        coVerify { repositoryMock.deleteProducto(p1) }
        // Verificamos que pidió recargar categorías (señal de que se refrescó la pantalla)
        coVerify(atLeast = 1) { repositoryMock.getCategorias() }
    }
}