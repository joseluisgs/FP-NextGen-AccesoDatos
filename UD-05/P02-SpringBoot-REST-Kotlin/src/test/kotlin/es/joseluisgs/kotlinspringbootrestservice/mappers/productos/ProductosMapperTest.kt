package es.joseluisgs.kotlinspringbootrestservice.mappers.productos

import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProductosMapperTest {
    final val categoriaTest = Categoria(100, "Categoria 100")
    final val productoTest = Producto("Producto 100", 100.0, categoriaTest)
    final val productoDTOTest = ProductoDTO(
        0,
        productoTest.nombre,
        productoTest.precio,
        productoTest.imagen,
        productoTest.createdAt,
        productoTest.slug,
        productoTest.categoria.nombre
    )

    private final val productosTest = listOf(productoTest)
    private final val productosDTOTests = listOf(productoDTOTest)

    private val productosMapper = ProductosMapper()

    @Test
    fun toDTOListTest() {
        assertEquals(productosDTOTests, productosMapper.toDTO(productosTest))
    }

    @Test
    fun toDTOTest() {
        assertEquals(productoDTOTest, productosMapper.toDTO(productoTest))
    }

    @Test
    fun fromDTOTest() {
        val createDTO = ProductoCreateDTO(productoTest.nombre, productoTest.precio, productoTest.categoria.id)
        val res = productosMapper.fromDTO(createDTO, categoriaTest)
        // la hora no va a coicidir por eso se comprueba solo el resto
        assertAll(
            { assertEquals(productoTest.nombre, res.nombre) },
            { assertEquals(productoTest.precio, res.precio) },
            { assertEquals(productoTest.categoria.id, res.categoria.id) }
        )
    }
}