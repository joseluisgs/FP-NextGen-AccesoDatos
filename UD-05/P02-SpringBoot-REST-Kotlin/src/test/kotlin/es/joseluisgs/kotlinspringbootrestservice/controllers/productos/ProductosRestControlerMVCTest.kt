package es.joseluisgs.kotlinspringbootrestservice.controllers.productos

import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.mappers.productos.ProductosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import es.joseluisgs.kotlinspringbootrestservice.repositories.categorias.CategoriasRepository
import es.joseluisgs.kotlinspringbootrestservice.repositories.productos.ProductosRepository
import es.joseluisgs.kotlinspringbootrestservice.services.storage.StorageService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ProductosRestControlerMVCTest
@Autowired constructor(
    @Autowired val mockMvc: MockMvc,
    @MockBean private val productosRepository: ProductosRepository,
    @MockBean private val productosMapper: ProductosMapper,
    @MockBean private val categoriasRepository: CategoriasRepository,
    @MockBean private val storageService: StorageService
) {
    final val categoriaTest = Categoria(100, "Categoria 100")
    final val productoTest = Producto("Producto 100", 100.0, categoriaTest)
    final val productoDTOTest = ProductoDTO(
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

    @Autowired
    private val jsonProductoCreateDTO: JacksonTester<ProductoCreateDTO>? = null

    @Test
    fun getAllTest() {
        val paging: Pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id")
        val pro: Page<Producto> = Mockito.mock(Page::class.java) as Page<Producto>

        Mockito.`when`(productosRepository.findAll(paging)).thenReturn(pro)
        Mockito.`when`(productosMapper.toDTO(productosTest)).thenReturn(productosDTOTests)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/rest/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        Mockito.verify(productosRepository, Mockito.times(1))
            .findAll(paging)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(listOf())
    }

    @Test
    fun getAllNameExceptionTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/rest/productos?nombre=paparruchas")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()
    }

    @Test
    fun findByIdTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.of(productoTest))
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/rest/productos/100")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.nombre").value(productoTest.nombre))
            .andExpect(jsonPath("$.precio").value(productoTest.precio))
            .andReturn()

        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    @Test
    fun findByIdExceptionTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.empty())
        // Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/rest/productos/100")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn()

        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
    }

    @Test
    fun createTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)

        val json = jsonProductoCreateDTO!!.write(createDTO).json

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.of(productoTest.categoria))
        Mockito.`when`(productosMapper.fromDTO(createDTO, productoTest.categoria))
            .thenReturn(productoTest)
        Mockito.`when`(productosRepository.save(productoTest)).thenReturn(productoTest)
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        mockMvc.perform(
            post("/rest/productos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            //.andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.nombre").value(productoDTOTest.nombre))
            .andExpect(jsonPath("$.precio").value(productoDTOTest.precio))
            .andExpect(jsonPath("$.categoria").value(productoDTOTest.categoria))
            .andReturn()


        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(productoTest.categoria.id)
        Mockito.verify(productosMapper, Mockito.times(1))
            .fromDTO(createDTO, productoTest.categoria)
        Mockito.verify(productosRepository, Mockito.times(1))
            .save(productoTest)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    @Test
    fun createPrecioExceptionTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, -19.00, productoTest.categoria.id)

        val json = jsonProductoCreateDTO!!.write(createDTO).json

        mockMvc.perform(
            post("/rest/productos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()
    }

    @Test
    fun createNombreExceptionTest() {
        val createDTO = ProductoCreateDTO("", 19.00, productoTest.categoria.id)

        val json = jsonProductoCreateDTO!!.write(createDTO).json

        mockMvc.perform(
            post("/rest/productos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()
    }

    @Test
    fun createCategoriaExceptionTest() {
        val createDTO = ProductoCreateDTO("Producto 100", 19.00, -9)

        val json = jsonProductoCreateDTO!!.write(createDTO).json

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.empty())

        mockMvc.perform(
            post("/rest/productos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(-9)
    }

    @Test
    fun updateTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)

        val json = jsonProductoCreateDTO!!.write(createDTO).json

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.of(productoTest.categoria))
        Mockito.`when`(productosRepository.findById(100))
            .thenReturn(Optional.of(productoTest))
        Mockito.`when`(productosMapper.fromDTO(createDTO, productoTest.categoria))
            .thenReturn(productoTest)
        Mockito.`when`(productosRepository.save(productoTest)).thenReturn(productoTest)
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        mockMvc.perform(
            put("/rest/productos/100")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.nombre").value("Producto 100"))
            .andReturn()

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

        val json = jsonProductoCreateDTO!!.write(createDTO).json

        Mockito.`when`(categoriasRepository.findById(productoTest.categoria.id))
            .thenReturn(Optional.empty())

        mockMvc.perform(
            put("/rest/productos/100")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(productoTest.categoria.id)
    }

    @Test
    fun deleteTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.of(productoTest))
        Mockito.`when`(productosMapper.toDTO(productoTest)).thenReturn(productoDTOTest)

        mockMvc.perform(
            delete("/rest/productos/100")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.nombre").value("Producto 100"))
            .andReturn()

        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(productosRepository, Mockito.times(1))
            .delete(productoTest)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productoTest)
    }

    @Test
    fun deleteExceptionTest() {
        Mockito.`when`(productosRepository.findById(100)).thenReturn(Optional.empty())

        Mockito.doNothing().`when`(productosRepository).delete(productoTest)

        mockMvc.perform(
            delete("/rest/productos/100")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn()

        Mockito.verify(productosRepository, Mockito.times(1))
            .findById(100)

        Mockito.verify(productosRepository, Mockito.times(0))
            .delete(productoTest)
    }

    @Test
    fun findBySlungTest() {
        val slug = "producto-100"
        Mockito.`when`(productosRepository.findAll())
            .thenReturn(listOf(productoTest))
        Mockito.`when`(productosMapper.toDTO(productosTest)).thenReturn(productosDTOTests)

        mockMvc.perform(
            get("/rest/productos/slug/$slug")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

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

        mockMvc.perform(
            get("/rest/productos/slug/$slug")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()

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

        mockMvc.perform(
            multipart("/rest/productos/create")
                .file(productoFile)
                .file(productoData)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.nombre").value(productoTest.nombre))
            .andExpect(jsonPath("$.precio").value(productoTest.precio))
            .andExpect(jsonPath("$.categoria").value(productoTest.categoria.nombre))
            .andReturn()


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