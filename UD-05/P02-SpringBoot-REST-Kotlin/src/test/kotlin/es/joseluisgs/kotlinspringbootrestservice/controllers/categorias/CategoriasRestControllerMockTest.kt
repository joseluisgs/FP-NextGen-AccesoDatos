package es.joseluisgs.kotlinspringbootrestservice.controllers.categorias

import es.joseluisgs.kotlinspringbootrestservice.dto.categorias.CategoriaCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.errors.categorias.CategoriaBadRequestException
import es.joseluisgs.kotlinspringbootrestservice.errors.categorias.CategoriaNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.mappers.productos.ProductosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import es.joseluisgs.kotlinspringbootrestservice.repositories.categorias.CategoriasRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.util.*


@SpringBootTest
class CategoriasRestControllerMockTest
@Autowired constructor(
    @InjectMocks private val categoriasRestController: CategoriasRestController,
    @MockBean private val categoriasRepository: CategoriasRepository,
    @MockBean private val productosMapper: ProductosMapper
) {

    private final val categoriaTest = Categoria(100, "Categoria 100")

    @Test
    fun getAllTest() {
        Mockito.`when`(categoriasRepository.findAll())
            .thenReturn(
                listOf(
                    Categoria(100, "Categoria 100"),
                    Categoria(200, "Categoria 200")
                )
            )
        val response = categoriasRestController.getAll()
        val res = response.body
        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(2, res?.size) }
        )

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findAll()
    }

    @Test
    fun findByIdTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.of(categoriaTest))

        val response = categoriasRestController.getById(100)
        val res = response.body
        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(100, res?.id) },
            { assertEquals("Categoria 100", res?.nombre) }
        )

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
    }

    @Test
    fun findByIdNotFoundTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.empty())

        val ex = assertThrows<CategoriaNotFoundException> {
            categoriasRestController.getById(100)
        }
        assertTrue(ex.message!!.contains("100"))

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
    }

    @Test
    fun createTest() {
        Mockito.`when`(categoriasRepository.save(Categoria("Categoria 100")))
            .thenReturn(categoriaTest)

        val response = categoriasRestController.create(CategoriaCreateDTO("Categoria 100"))
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.CREATED.value(), response.statusCode.value()) },
            { assertEquals(100, res?.id) },
            { assertEquals("Categoria 100", res?.nombre) }
        )

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .save(Categoria("Categoria 100"))
    }

    @Test
    fun createTestBlackNameTest() {
        Mockito.`when`(categoriasRepository.save(Categoria("")))
            .thenReturn(Categoria(100, ""))

        val ex = assertThrows<CategoriaBadRequestException> {
            categoriasRestController.create(CategoriaCreateDTO(""))
        }
        assertTrue(ex.message!!.contains("nombre es obligatorio"))
    }

    @Test
    fun updateTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.of(categoriaTest))
        Mockito.`when`(categoriasRepository.save(categoriaTest))
            .thenReturn(categoriaTest)

        val response = categoriasRestController.update(CategoriaCreateDTO("Categoria 100"), 100)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(100, res?.id) },
            { assertEquals("Categoria 100", res?.nombre) }
        )

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .save(categoriaTest)
    }

    @Test
    fun updateTestNotFoundTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.empty())

        val ex = assertThrows<CategoriaNotFoundException> {
            categoriasRestController.update(CategoriaCreateDTO("Categoria 100"), 100)
        }
        assertTrue(ex.message!!.contains("100"))

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
    }

    @Test
    fun deleteTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.of(categoriaTest))

        Mockito.`when`(categoriasRepository.countByProductos(100))
            .thenReturn(0)

        val response = categoriasRestController.delete(100)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(100, res?.id) },
            { assertEquals("Categoria 100", res?.nombre) }
        )

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .countByProductos(100)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .delete(categoriaTest)
    }

    @Test
    fun deleteTestNotFoundTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.empty())

        val ex = assertThrows<CategoriaNotFoundException> {
            categoriasRestController.delete(100)
        }
        assertTrue(ex.message!!.contains("100"))

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
    }

    @Test
    fun deleteTestHasProductosTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.of(categoriaTest))
        Mockito.`when`(categoriasRepository.countByProductos(100))
            .thenReturn(1)

        val ex = assertThrows<CategoriaBadRequestException> {
            categoriasRestController.delete(100)
        }
        assertTrue(ex.message!!.contains("100"))

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .countByProductos(100)
    }

    @Test
    fun getProductosTest() {
        val producto = Producto("Producto 100", 100.0, categoriaTest)
        val productoDTO = ProductoDTO(
            100,
            producto.nombre,
            producto.precio,
            producto.imagen,
            producto.createdAt,
            producto.slug,
            producto.categoria.nombre
        )

        val productos = listOf(producto)
        val productosDTO = listOf(productoDTO)

        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.of(categoriaTest))
        Mockito.`when`(categoriasRepository.findProductosByCategoria(100))
            .thenReturn(productos)
        Mockito.`when`(productosMapper.toDTO(productos))
            .thenReturn(productosDTO)

        val response = categoriasRestController.getProductos(100)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(1, res?.productos?.size) },
            { assertEquals("Producto 100", res?.productos?.get(0)?.nombre) }
        )

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findProductosByCategoria(100)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productos)
    }
}