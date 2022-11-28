package es.joseluisgs.kotlinspringbootrestservice.controllers.categorias

import es.joseluisgs.kotlinspringbootrestservice.dto.categorias.CategoriaCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.mappers.productos.ProductosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import es.joseluisgs.kotlinspringbootrestservice.repositories.categorias.CategoriasRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CategoriasRestControllerMVCTest
@Autowired constructor(
    @Autowired val mockMvc: MockMvc,
    @MockBean private val categoriasRepository: CategoriasRepository,
    @MockBean private val productosMapper: ProductosMapper
) {

    @Autowired
    private val jsonCategoriaCreateDTO: JacksonTester<CategoriaCreateDTO>? = null

    private final val categoriaTest = Categoria(100, "Categoria 100")

    @Test
    fun getAllTest() {
        Mockito.`when`(categoriasRepository.findAll()).thenReturn(listOf(categoriaTest))
        mockMvc.perform(
            get("/rest/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$", hasSize<Any>(1)))
            .andExpect(jsonPath("\$.[0].id").value(categoriaTest.id))
            .andExpect(jsonPath("\$.[0].nombre").value(categoriaTest.nombre))
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1)).findAll()
    }

    @Test
    fun getByIdTest() {
        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.of(categoriaTest))
        mockMvc.perform(
            get("/rest/categorias/{id}", categoriaTest.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(categoriaTest.id))
            .andExpect(jsonPath("\$.nombre").value(categoriaTest.nombre))
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1)).findById(categoriaTest.id)
    }

    @Test
    fun getByIdNotFoundTest() {
        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.empty())
        mockMvc.perform(
            get("/rest/categorias/{id}", categoriaTest.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1)).findById(categoriaTest.id)
    }

    @Test
    fun createTest() {
        val createDTO = CategoriaCreateDTO(categoriaTest.nombre)

        val json = jsonCategoriaCreateDTO!!.write(createDTO).json

        Mockito.`when`(categoriasRepository.save(Categoria(categoriaTest.nombre)))
            .thenReturn(categoriaTest)

        mockMvc.perform(
            post("/rest/categorias")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("\$.id").value(categoriaTest.id))
            .andExpect(jsonPath("\$.nombre").value(categoriaTest.nombre))
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .save(Categoria(categoriaTest.nombre))
    }

    @Test
    fun createTestBlackNameTest() {
        val createDTO = CategoriaCreateDTO("")

        val json = jsonCategoriaCreateDTO!!.write(createDTO).json

        mockMvc.perform(
            post("/rest/categorias")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(0))
            .save(Categoria(categoriaTest.nombre))
    }

    @Test
    fun updateCategoria() {
        val updateDTO = CategoriaCreateDTO(categoriaTest.nombre)

        val json = jsonCategoriaCreateDTO!!.write(updateDTO).json

        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.of(categoriaTest))

        Mockito.`when`(categoriasRepository.save(Categoria(categoriaTest.id, updateDTO.nombre)))
            .thenReturn(categoriaTest)

        mockMvc.perform(
            put("/rest/categorias/{id}", categoriaTest.id)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(categoriaTest.id))
            .andExpect(jsonPath("\$.nombre").value(categoriaTest.nombre))
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(categoriaTest.id)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .save(Categoria(categoriaTest.id, updateDTO.nombre))
    }

    @Test
    fun updateCategoriaNotFoundTest() {
        val updateDTO = CategoriaCreateDTO(categoriaTest.nombre)

        val json = jsonCategoriaCreateDTO!!.write(updateDTO).json
        println(json)

        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.empty())

        mockMvc.perform(
            put("/rest/categorias/{id}", categoriaTest.id)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(categoriaTest.id)
        Mockito.verify(categoriasRepository, Mockito.times(0))
            .save(Categoria(categoriaTest.id, updateDTO.nombre))
    }

    @Test
    fun updateBlackNameTest() {
        val updateDTO = CategoriaCreateDTO("")

        val json = jsonCategoriaCreateDTO!!.write(updateDTO).json
        println(json)

        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.of(categoriaTest))

        mockMvc.perform(
            put("/rest/categorias/{id}", categoriaTest.id)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(0))
            .findById(categoriaTest.id)
        Mockito.verify(categoriasRepository, Mockito.times(0))
            .save(Categoria(categoriaTest.id, updateDTO.nombre))
    }

    @Test
    fun deleteTest() {
        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.of(categoriaTest))

        mockMvc.perform(
            delete("/rest/categorias/{id}", categoriaTest.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(categoriaTest.id))
            .andExpect(jsonPath("\$.nombre").value(categoriaTest.nombre))
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(categoriaTest.id)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .delete(categoriaTest)
    }

    @Test
    fun deleteNotFoundTest() {
        Mockito.`when`(categoriasRepository.findById(categoriaTest.id))
            .thenReturn(Optional.empty())

        mockMvc.perform(
            delete("/rest/categorias/{id}", categoriaTest.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(categoriaTest.id)
        Mockito.verify(categoriasRepository, Mockito.times(0))
            .delete(categoriaTest)
    }

    @Test
    fun deleteTestHasProductosTest() {
        Mockito.`when`(categoriasRepository.findById(100))
            .thenReturn(Optional.of(categoriaTest))
        Mockito.`when`(categoriasRepository.countByProductos(100))
            .thenReturn(1)

        mockMvc.perform(
            delete("/rest/categorias/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andReturn()

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


        mockMvc.perform(
            get("/rest/categorias/{id}/productos", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(categoriaTest.id))
            .andExpect(jsonPath("\$.nombre").value(categoriaTest.nombre))
            .andExpect(jsonPath("\$.productos", hasSize<Any>(1)))
            .andExpect(jsonPath("\$.productos[0].id").value(productoDTO.id))
            .andExpect(jsonPath("\$.productos[0].nombre").value(productoDTO.nombre))
            .andExpect(jsonPath("\$.productos[0].precio").value(productoDTO.precio))
            .andReturn()

        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(categoriasRepository, Mockito.times(1))
            .findProductosByCategoria(100)
        Mockito.verify(productosMapper, Mockito.times(1))
            .toDTO(productos)
    }
}