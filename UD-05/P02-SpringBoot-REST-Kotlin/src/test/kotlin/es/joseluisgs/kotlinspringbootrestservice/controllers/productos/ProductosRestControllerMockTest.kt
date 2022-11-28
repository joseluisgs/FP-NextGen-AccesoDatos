package es.joseluisgs.kotlinspringbootrestservice.controllers.productos

import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoListDTO
import es.joseluisgs.kotlinspringbootrestservice.errors.productos.ProductoBadRequestException
import es.joseluisgs.kotlinspringbootrestservice.errors.productos.ProductoNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.mappers.productos.ProductosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import es.joseluisgs.kotlinspringbootrestservice.repositories.categorias.CategoriasRepository
import es.joseluisgs.kotlinspringbootrestservice.repositories.productos.ProductosRepository
import es.joseluisgs.kotlinspringbootrestservice.services.storage.StorageService
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import java.util.*


@SpringBootTest
class ProductosRestControllerMockTest
@Autowired constructor(
    @InjectMocks private val productosRestController: ProductosRestController,
    @MockBean private val productosRepository: ProductosRepository,
    @MockBean private val productosMapper: ProductosMapper,
    @MockBean private val categoriasRepository: CategoriasRepository,
    @MockBean private val storageService: StorageService
) {
    private final val categoriaTest = Categoria(100, "Categoria 100")
    private final val productoTest = Producto("Producto 100", 100.0, categoriaTest)
    private final val productoDTOTest = ProductoDTO(
        100,
        productoTest.nombre,
        productoTest.precio,
        productoTest.imagen,
        productoTest.createdAt,
        productoTest.slug,
        productoTest.categoria.nombre
    )

    private final val productosTest = listOf(productoTest)
    private final val productosDTOTests = listOf(productoDTOTest)

    @Test
    fun getAllTest() {
        val paging: Pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id")
        val pro: Page<Producto> = Mockito.mock(Page::class.java) as Page<Producto>

        val result = ProductoListDTO(
            data = productosDTOTests,
            currentPage = 0,
            totalPages = 1,
            totalElements = 1,
            sort = "id"
        )

        Mockito.`when`(productosRepository.findAll(paging)).thenReturn(pro)
        Mockito.`when`(productosMapper.toDTO(productosTest)).thenReturn(productosDTOTests)

        val response = productosRestController.getAll(null, 0, 10, "id", null)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
        )
        Mockito.verify(productosRepository, Mockito.times(1))
            .findAll(paging)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(listOf())
    }

    @Test
    fun getAllNameTest() {
        val paging: Pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id")
        val pro: Page<Producto?> = Mockito.mock(Page::class.java) as Page<Producto?>


        val nombre = "nombre"
        Mockito.`when`(productosRepository.findByNombreContainsIgnoreCase(nombre, paging)).thenReturn(pro)
        Mockito.`when`(productosMapper.toDTO(productosTest)).thenReturn(productosDTOTests)

        val response = productosRestController.getAll(nombre, 0, 10, "id", null)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
        )
        Mockito.verify(productosRepository, Mockito.times(1))
            .findByNombreContainsIgnoreCase(nombre, paging)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(listOf())
    }

    @Test
    fun getAllNameExceptionTest() {
        val paging: Pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id")

        val ex = assertThrows<ProductoBadRequestException> {
            productosRestController.getAll("paparruchas", 0, 10, "id", null)
        }
        assertTrue(ex.message!!.contains("Parámetros de consulta incorrectos"))
    }

    @Test
    fun findByIdTest() {

        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.of(productoTest))
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        val response = productosRestController.findById(100)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(productoDTOTest, res) }
        )
        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    @Test
    fun findByIdNotFoundTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.empty())

        val ex = assertThrows<ProductoNotFoundException> {
            productosRestController.findById(100)
        }
        assertTrue(ex.message!!.contains("100"))
    }

    @Test
    fun createTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.of(productoTest.categoria))
        Mockito.`when`(productosMapper.fromDTO(createDTO, productoTest.categoria))
            .thenReturn(productoTest)
        Mockito.`when`(productosRepository.save(productoTest)).thenReturn(productoTest)
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        val response = productosRestController.create(createDTO)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.CREATED.value(), response.statusCode.value()) },
            { assertEquals(productoDTOTest, res) }
        )
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(productoTest.categoria.id)
        Mockito.verify(productosMapper, Mockito.times(1))
            .fromDTO(createDTO, productoTest.categoria)
        Mockito.verify(productosRepository, Mockito.times(1))
            .save(productoTest)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    // Lo voy a hacer con solo el precio, pero deberia hacerse con el nombre, precio y categoria
    @Test
    fun createPrecioExceptionTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, -19.00, productoTest.categoria.id)

//        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
//            .thenReturn(Optional.empty())

        val ex = assertThrows<ProductoBadRequestException> {
            productosRestController.create(createDTO)
        }
        assertTrue(ex.message!!.contains("El precio debe ser mayor que 0"))
    }

    @Test
    fun createNombreExceptionTest() {
        val createDTO = ProductoCreateDTO("", 9.00, productoTest.categoria.id)

//        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
//            .thenReturn(Optional.empty())

        val ex = assertThrows<ProductoBadRequestException> {
            productosRestController.create(createDTO)
        }
        assertTrue(ex.message!!.contains("El nombre es obligatorio"))
    }

    @Test
    fun createCategoriaExceptionTest() {
        val createDTO = ProductoCreateDTO("pepe", 9.00, -9)

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.empty())

        val ex = assertThrows<ProductoBadRequestException> {
            productosRestController.create(createDTO)
        }
        assertTrue(ex.message!!.contains("No existe categoría con id"))

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(-9)
    }

    @Test
    fun updateTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.of(productoTest.categoria))
        Mockito.`when`(productosRepository.findById(100))
            .thenReturn(Optional.of(productoTest))
        Mockito.`when`(productosMapper.fromDTO(createDTO, productoTest.categoria))
            .thenReturn(productoTest)
        Mockito.`when`(productosRepository.save(productoTest)).thenReturn(productoTest)
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        val response = productosRestController.update(createDTO, 100)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(productoDTOTest, res) }
        )
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(productoTest.categoria.id)
        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(productosRepository, Mockito.times(1))
            .save(productoTest)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    @Test
    fun updateExceptionTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.empty())

        val ex = assertThrows<ProductoBadRequestException> {
            productosRestController.update(createDTO, 100)
        }
        assertTrue(ex.message!!.contains("No existe categoría con id"))

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(productoTest.categoria.id)
    }

    @Test
    fun deleteTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.of(productoTest))
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)
        Mockito.doNothing().`when`(productosRepository).delete(productoTest)

        val response = productosRestController.delete(100)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(productoDTOTest, res) }
        )
        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(productosRepository, Mockito.times(1))
            .delete(productoTest)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
        Mockito.verify(productosRepository, Mockito.times(1))
            .delete(productoTest)
    }

    @Test
    fun deleteExceptionTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.empty())

        val ex = assertThrows<ProductoNotFoundException> {
            productosRestController.delete(100)
        }
        assertTrue(ex.message!!.contains("No se puede encontrar el producto"))

        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
    }

    @Test
    fun findBySlungTest() {
        val slug = "producto-100"
        Mockito.`when`(productosRepository.findAll())
            .thenReturn(listOf(productoTest))
        Mockito.`when`(productosMapper.toDTO(productosTest)).thenReturn(productosDTOTests)

        val response = productosRestController.findBySlug(slug)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
        )
        Mockito.verify(productosRepository, Mockito.times(1))
            .findAll()
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    @Test
    fun findBySlungExceptionTest() {
        val slug = "producto"
        Mockito.`when`(productosRepository.findAll())
            .thenReturn(listOf(productoTest))

        val ex = assertThrows<ProductoBadRequestException> {
            productosRestController.findBySlug(slug)
        }

        Mockito.verify(productosRepository, Mockito.times(1))
            .findAll()
    }

    @Test
    fun createWithImageTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)
        val imagen = "1646245208061_nuevoproducto.jpg"
        val urlImagen = "http://localhost:6969/rest/storage/1646245208061_nuevoproducto.jpg"

        val productoFile = MockMultipartFile("file", "nuevoproducto.jpg", "text/plain", "some info".toByteArray())
        val productoData = MockMultipartFile(
            "producto", "", "application/json",
            """
                {
                  "nombre": "${createDTO.nombre}",
                  "precio": "${createDTO.precio}",
                  "categoriaId": "${createDTO.categoriaId}"
                }
            """.trimIndent().toByteArray()
        )

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.of(productoTest.categoria))
        Mockito.`when`(productosMapper.fromDTO(createDTO, productoTest.categoria))
            .thenReturn(productoTest)
        Mockito.`when`(productosRepository.save(productoTest))
            .thenReturn(productoTest)
        Mockito.`when`(productosMapper.toDTO(productoTest))
            .thenReturn(productoDTOTest)
        Mockito.`when`(storageService.store(productoFile))
            .thenReturn(imagen)
        Mockito.`when`(storageService.getUrl(imagen))
            .thenReturn(urlImagen)

        val response = productosRestController.createWithImage(createDTO, productoFile)
        val res = response.body

        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { assertEquals(productoDTOTest, res) }
        )
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(productoTest.categoria.id)
        Mockito.verify(productosMapper, Mockito.times(1))
            .fromDTO(createDTO, productoTest.categoria)
        Mockito.verify(productosRepository, Mockito.times(1))
            .save(productoTest)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
        Mockito.verify(storageService, Mockito.times(1))
            .store(productoFile)
        Mockito.verify(storageService, Mockito.times(1))
            .getUrl(imagen)
    }
}