package es.joseluisgs.kotlinspringbootrestservice.repositories.productos

import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class ProductosRepositoryTest
@Autowired constructor(
    private val entityManager: TestEntityManager,
    private val productosRepository: ProductosRepository
) {
    private val categoriaTest = Categoria(nombre = "Categoria 99")
    private val productoTest = Producto(nombre = "Producto 99", precio = 99.99, categoria = categoriaTest)

    @Test
    fun findByIdTest() {
        entityManager.persist(categoriaTest)
        entityManager.persist(productoTest)
        entityManager.flush()
        val res = productosRepository.findByIdOrNull(productoTest.id)!!
        assertEquals(res, productoTest)
        assert(res.nombre == productoTest.nombre)
    }

    @Test
    fun saveTest() {
        entityManager.persist(categoriaTest)
        entityManager.flush()
        val res = productosRepository.save(productoTest)
        assertEquals(res, productoTest)
        assert(res.nombre == productoTest.nombre)
    }

    @Test
    fun updateTest() {
        entityManager.persist(categoriaTest)
        entityManager.persist(productoTest)
        entityManager.flush()
        var res = productosRepository.findByIdOrNull(productoTest.id)!!
        res.nombre = "Producto 100"
        res = productosRepository.save(res)
        assertAll(
            { assertEquals(res, productoTest) },
            { assert(res.nombre == productoTest.nombre) }
        )
    }

    @Test
    fun deleteTest() {
        entityManager.persist(categoriaTest)
        entityManager.persist(productoTest)
        entityManager.flush()
        productosRepository.delete(productoTest)
        productosRepository.findByIdOrNull(productoTest.id)?.let {
            assertEquals(it, null)
        }
    }

    @Test
    fun findByNombreContainsIgnoreCaseTest() {
        entityManager.persist(categoriaTest)
        entityManager.persist(productoTest)
        entityManager.flush()
        val paging: Pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id")
        val res = productosRepository.findByNombreContainsIgnoreCase("producto", paging)
        assertAll(
            { assertTrue(res.content.contains(productoTest)) },
            { assertEquals(res.totalElements, 1) }
        )
    }
}