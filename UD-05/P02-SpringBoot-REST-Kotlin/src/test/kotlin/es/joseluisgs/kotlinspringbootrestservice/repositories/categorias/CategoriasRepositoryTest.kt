package es.joseluisgs.kotlinspringbootrestservice.repositories.categorias

import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CategoriasRepositoryTest
@Autowired constructor(
    private val entityManager: TestEntityManager,
    private val categoriasRepository: CategoriasRepository
) {

    private val categoriaTest = Categoria(nombre = "Categoria 99")
    private val productoTest = Producto(nombre = "Producto 99", 99.9, categoriaTest)
    /*@BeforeAll
    fun setup() {
        println(">> Setup")
    }

    @AfterAll
    fun teardown() {
        println(">> Tear down")
    }*/

    @Test
    // @Order(1)
    fun findByIdTest() {
        entityManager.persist(categoriaTest)
        entityManager.flush()
        val res = categoriasRepository.findByIdOrNull(categoriaTest.id)!!
        assertEquals(res, categoriaTest)
    }

    @Test
    // @Order(2)
    fun saveTest() {
        val found = categoriasRepository.save(categoriaTest)
        assertEquals(found, categoriaTest)
    }

    @Test
    // @Order(3)
    fun updateTest() {
        entityManager.persist(categoriaTest)
        entityManager.flush()
        var res = categoriasRepository.findByIdOrNull(categoriaTest.id)!!
        res.nombre = "Categoria 100"
        res = categoriasRepository.save(categoriaTest)
        assertEquals(res, categoriaTest)
    }

    @Test
    // @Order(4)
    fun deleteTest() {
        entityManager.persist(categoriaTest)
        entityManager.flush()
        val res = categoriasRepository.findByIdOrNull(categoriaTest.id)!!
        categoriasRepository.delete(res)
        categoriasRepository.findByIdOrNull(categoriaTest.id)?.let {
            assertEquals(it, null)
        }
    }

    @Test
    fun countByProductosTest() {
        entityManager.persist(categoriaTest)
        entityManager.persist(productoTest)
        entityManager.flush()
        val cate = categoriasRepository.findByIdOrNull(categoriaTest.id)!!
        val res = categoriasRepository.countByProductos(cate.id)
        assertEquals(res, 1)
    }

    @Test
    fun findProductosByCategoria() {
        entityManager.persist(categoriaTest)
        entityManager.persist(productoTest)
        entityManager.flush()
        val res = categoriasRepository.findProductosByCategoria(categoriaTest.id)
        assertEquals(res, listOf(productoTest))
    }
}
