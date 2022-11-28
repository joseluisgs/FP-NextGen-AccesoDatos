package es.joseluisgs.kotlinspringbootrestservice.controllers.pedidos

import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.*
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.usuarios.UsuarioPedidoDTO
import es.joseluisgs.kotlinspringbootrestservice.errors.pedidos.PedidoNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.mappers.pedidos.PedidosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.*
import es.joseluisgs.kotlinspringbootrestservice.repositories.pedidos.PedidosRepository
import es.joseluisgs.kotlinspringbootrestservice.repositories.productos.ProductosRepository
import es.joseluisgs.kotlinspringbootrestservice.repositories.usuarios.UsuariosRepository
import org.junit.jupiter.api.Assertions
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
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class PedidosRestControllerMockTest
@Autowired constructor(
    @InjectMocks private val pedidosRestController: PedidosRestController,
    @MockBean private val pedidosRepository: PedidosRepository,
    @MockBean private val productosRepository: ProductosRepository,
    @MockBean private val pedidosMapper: PedidosMapper,
    @MockBean private val usuariosRepository: UsuariosRepository
) {
    final val cliente = Usuario(
        id = 100,
        username = "Prueba",
        fullName = "Prueba",
        password = "1234",
        email = "test@test.com",
        roles = setOf(Rol.USER),
        createdAt = LocalDateTime.now(),
        lastPasswordChangeAt = LocalDateTime.now(),
        avatar = "",
    )

    final val categoriaTest = Categoria(100, "Categoria 100")

    final val productoTest = Producto("Producto 100", 100.0, categoriaTest)

    private final val productoDTOTest = ProductoDTO(
        100,
        productoTest.nombre,
        productoTest.precio,
        productoTest.imagen,
        productoTest.createdAt,
        productoTest.slug,
        productoTest.categoria.nombre
    )

    final val lineasPedidoTest = LineaPedido(
        id = 100,
        precio = 100.0,
        cantidad = 1,
        producto = productoTest,
    )

    private final val pedidoTest = Pedido(
        id = 100,
        fecha = LocalDateTime.now(),
        cliente = cliente,
        lineasPedido = mutableListOf(lineasPedidoTest),
    )
    private final val lineaPedidoDTO = LineaPedidoDTO(
        id = lineasPedidoTest.id,
        precio = lineasPedidoTest.precio,
        cantidad = lineasPedidoTest.cantidad,
        subTotal = lineasPedidoTest.subTotal,
        producto = productoDTOTest,
    )

    private final val pedidoDTOTest = PedidoDTO(
        id = pedidoTest.id,
        fecha = pedidoTest.fecha,
        total = pedidoTest.total,
        cliente = UsuarioPedidoDTO(
            username = cliente.username,
            fullName = cliente.fullName,
            email = cliente.email,
        ),
        lineasPedido = listOf(lineaPedidoDTO),
    )

    private final val pedidosTest = listOf(pedidoTest)
    private final val pedidosDTOTests = listOf(pedidoDTOTest)

    private final val pedidoCreateDTOTest = PedidoCreateDTO(
        clienteId = cliente.id,
        lineasPedido = listOf(
            LineaPedidoCreateDTO(
                productoId = lineasPedidoTest.producto.id,
                cantidad = lineasPedidoTest.cantidad
            )
        ),
    )

    @Test
    fun getAllTest() {
        val paging: Pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id")
        val ped: Page<Pedido> = Mockito.mock(Page::class.java) as Page<Pedido>

        val result = PedidoListDTO(
            data = pedidosDTOTests,
            currentPage = 0,
            totalPages = 1,
            totalElements = 1,
            sort = "id"
        )

        Mockito.`when`(pedidosRepository.findAll(paging)).thenReturn(ped)
        Mockito.`when`(pedidosMapper.toDTO(pedidosTest)).thenReturn(pedidosDTOTests)

        val response = pedidosRestController.getAll(0, 10, "id", null)
        val res = response.body

        assertAll(
            { Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
        )
        Mockito.verify(pedidosRepository, Mockito.times(1))
            .findAll(paging)
        Mockito.verify(pedidosMapper, Mockito.times(1))
            .toDTO(listOf())
    }

    @Test
    fun findByIdTest() {

        Mockito.`when`(pedidosRepository.findById(100)).thenReturn(Optional.of(pedidoTest))
        Mockito.`when`(pedidosMapper.toDTO(pedidoTest)).thenReturn(pedidoDTOTest)

        val response = pedidosRestController.findById(100)
        val res = response.body

        assertAll(
            { Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode.value()) },
            { Assertions.assertEquals(pedidoDTOTest, res) }
        )
        Mockito.verify(pedidosRepository, Mockito.times(1))
            .findById(100)
        Mockito.verify(pedidosMapper, Mockito.times(1))
            .toDTO(pedidoTest)
    }

    @Test
    fun findByIdNotFoundTest() {
        Mockito.`when`(pedidosRepository.findById(100)).thenReturn(Optional.empty())

        val ex = assertThrows<PedidoNotFoundException> {
            pedidosRestController.findById(100)
        }
        Assertions.assertTrue(ex.message!!.contains("100"))
    }

    /* @Test
     fun createTest() {
         Mockito.`when`(usuariosRepository.findById(pedidoCreateDTOTest.clienteId))
             .thenReturn(Optional.of(cliente))
         Mockito.`when`(productosRepository.findById(pedidoCreateDTOTest.lineasPedido[0].productoId))
             .thenReturn(Optional.of(productoTest))

         // Otra forma usando Mockito Kotlin
         whenever(pedidosRepository.save(pedidoTest)).thenReturn(pedidoTest)

         Mockito.`when`(pedidosMapper.toDTO(pedidoTest)).thenReturn(pedidoDTOTest)

         val response = pedidosRestController.create(pedidoCreateDTOTest)
         val res = response.body

         assertAll(
             { Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode.value()) },
             { Assertions.assertEquals(pedidoDTOTest, res) }
         )

         Mockito.verify(usuariosRepository, Mockito.times(1))
             .findById(cliente.id)
         Mockito.verify(productosRepository, Mockito.times(1))
             .findById(productoTest.id)
         Mockito.verify(pedidosRepository, Mockito.times(1))
             .save(pedidoTest)
         Mockito.verify(pedidosMapper, Mockito.times(1))
             .toDTO(pedidoTest)
     }*/
}