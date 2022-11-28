package es.joseluisgs.kotlinspringbootrestservice.controllers.pedidos

import es.joseluisgs.kotlinspringbootrestservice.config.APIConfig
import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.LineaPedidoCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.PedidoCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.PedidoDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.PedidoListDTO
import es.joseluisgs.kotlinspringbootrestservice.errors.pedidos.LineaPedidoNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.errors.pedidos.PedidoBadRequestException
import es.joseluisgs.kotlinspringbootrestservice.errors.pedidos.PedidoNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.errors.pedidos.PedidosNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.errors.productos.ProductoBadRequestException
import es.joseluisgs.kotlinspringbootrestservice.errors.productos.ProductoNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.errors.usuarios.UsuarioNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.mappers.pedidos.PedidosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.LineaPedido
import es.joseluisgs.kotlinspringbootrestservice.models.Pedido
import es.joseluisgs.kotlinspringbootrestservice.repositories.pedidos.PedidosRepository
import es.joseluisgs.kotlinspringbootrestservice.repositories.productos.ProductosRepository
import es.joseluisgs.kotlinspringbootrestservice.repositories.usuarios.UsuariosRepository
import es.joseluisgs.kotlinspringbootrestservice.utils.pagination.PaginationLinks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(APIConfig.API_PATH + "/pedidos")
// Le aplico DI por constructor
class PedidosRestController
@Autowired constructor(
    private val pedidosRepository: PedidosRepository,
    private val pedidosMapper: PedidosMapper,
    private val paginationLinks: PaginationLinks,
    private val productosRepository: ProductosRepository,
    private val usuariosRepository: UsuariosRepository
) {

    @RequestMapping("")
    fun getAll(
        // Otra forma de ponerlo sería
        // @PageableDefault(size = 10, page = 0) pageable: Pageable
        @RequestParam(defaultValue = APIConfig.PAGINATION_INIT) page: Int,
        @RequestParam(defaultValue = APIConfig.PAGINATION_SIZE) size: Int,
        @RequestParam(defaultValue = APIConfig.PAGINATION_SORT) sort: String,
        request: HttpServletRequest?
    ): ResponseEntity<PedidoListDTO> {

        val paging: Pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort)

        val pagedResult = pedidosRepository.findAll(paging)

        // Si no hay nada, excepción de error
        if (pagedResult.isEmpty) {
            throw PedidosNotFoundException()
        }

        // si hay, vamos con la salida y con todos los links de paginaciones
        var links = ""
        if (request != null) {
            val uriBuilder = UriComponentsBuilder.fromHttpUrl(request.requestURL.toString())
            links = paginationLinks.createLinkHeader(pagedResult, uriBuilder)
        }

        val result = PedidoListDTO(
            data = pedidosMapper.toDTO(pagedResult.content),
            currentPage = pagedResult.number,
            totalPages = pagedResult.totalPages,
            totalElements = pagedResult.totalElements,
            sort = sort,
            links = links
        )

        return ResponseEntity
            .ok()
            .header("link", links)
            .body(result)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<PedidoDTO> {
        val pedido = pedidosRepository.findById(id).orElseGet { throw PedidoNotFoundException(id) }
        return ResponseEntity.ok(pedidosMapper.toDTO(pedido))
    }

    @PostMapping("")
    fun create(@RequestBody pedido: PedidoCreateDTO): ResponseEntity<PedidoDTO> {
        // Obtenemos el cliente
        val cliente = usuariosRepository.findById(pedido.clienteId).orElseThrow {
            throw UsuarioNotFoundException(pedido.clienteId)
        }

        // Obtenemos las lineas del pedido
        val lineasPedido = pedido.lineasPedido.map { linea ->
            // Buscamos el producto
            val producto = productosRepository.findById(linea.productoId).orElseThrow {
                throw ProductoNotFoundException(linea.productoId)
            }
            // Lo añadimos al pedido
            LineaPedido(producto.precio, linea.cantidad, producto)
        }.toMutableList()

        // Creamos el pedido
        val pedido = Pedido(cliente)

        // Añadimos las lineas
        lineasPedido.forEach { linea ->
            pedido.addLineaPedido(linea)
        }

        // Salvamos el pedido y resultado
        try {
            val result = pedidosMapper.toDTO(pedidosRepository.save(pedido))
            return ResponseEntity.status(HttpStatus.CREATED).body(result)
        } catch (e: Exception) {
            throw PedidoBadRequestException(
                "Error: Insertar Pedido",
                "Campos incorrectos. ${e.message}"
            )
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<PedidoDTO> {
        val pedido = pedidosRepository.findById(id).orElseGet { throw PedidoNotFoundException(id) }
        try {
            pedidosRepository.delete(pedido)
            return ResponseEntity.ok(pedidosMapper.toDTO(pedido))
        } catch (e: Exception) {
            throw ProductoBadRequestException(
                "Error: Eliminar Pedido",
                "Id de pedido no existe. ${e.message}"
            )
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody pedidoDTO: PedidoCreateDTO): ResponseEntity<PedidoDTO> {
        val pedido = pedidosRepository.findById(id).orElseGet { throw PedidoNotFoundException(id) }
        // si hay nuevo cliente lo buscamos

        val cliente = usuariosRepository.findById(pedidoDTO.clienteId).orElseThrow {
            throw UsuarioNotFoundException(pedidoDTO.clienteId)
        }

        // Obtenemos las lineas del pedido
        val lineasPedido = pedidoDTO.lineasPedido.map { linea ->
            // Buscamos el producto
            val producto = productosRepository.findById(linea.productoId).orElseThrow {
                throw ProductoNotFoundException(linea.productoId)
            }
            // Lo añadimos al pedido
            LineaPedido(producto.precio, linea.cantidad, producto)
        }.toMutableList()

        // Creamos el pedido
        pedido.cliente = cliente
        pedido.lineasPedido.clear()

        // Añadimos las lineas
        lineasPedido.forEach { linea ->
            pedido.addLineaPedido(linea)
        }

        // Salvamos el pedido y resultado
        try {
            val result = pedidosMapper.toDTO(pedidosRepository.save(pedido))
            return ResponseEntity.status(HttpStatus.OK).body(result)
        } catch (e: Exception) {
            throw PedidoBadRequestException(
                "Error: Actualizar Pedido",
                "${e.message}"
            )
        }
    }

    // ahora vamos a hacer el crud sobre pedidos para añadir la línea de pedido
    @PostMapping("/{id}/lineas")
    fun addLineaPedido(
        @PathVariable id: Long,
        @RequestBody lineaPedidoDTO: LineaPedidoCreateDTO
    ): ResponseEntity<PedidoDTO> {
        val pedido = pedidosRepository.findById(id).orElseGet { throw PedidoNotFoundException(id) }
        // Buscamos el producto
        val producto = productosRepository.findById(lineaPedidoDTO.productoId).orElseThrow {
            throw ProductoNotFoundException(lineaPedidoDTO.productoId)
        }
        // Lo añadimos al pedido
        val lineaPedido = LineaPedido(producto.precio, lineaPedidoDTO.cantidad, producto)
        pedido.addLineaPedido(lineaPedido)
        // Salvamos el pedido y resultado
        try {
            val result = pedidosMapper.toDTO(pedidosRepository.save(pedido))
            return ResponseEntity.status(HttpStatus.OK).body(result)
        } catch (e: Exception) {
            throw PedidoBadRequestException(
                "Error: Añadir Linea Pedido",
                "${e.message}"
            )
        }
    }

    @DeleteMapping("/{id}/lineas/{lineaId}")
    fun deleteLineaPedido(
        @PathVariable id: Long,
        @PathVariable lineaId: Long
    ): ResponseEntity<PedidoDTO> {
        val pedido = pedidosRepository.findById(id).orElseGet { throw PedidoNotFoundException(id) }
        // Buscamos el producto
        val lineaPedido = pedido.lineasPedido.find { it.id == lineaId } ?: throw LineaPedidoNotFoundException(lineaId)

        pedido.removeLineaPedido(lineaPedido)
        // Salvamos el pedido y resultado
        try {
            val result = pedidosMapper.toDTO(pedidosRepository.save(pedido))
            return ResponseEntity.status(HttpStatus.OK).body(result)
        } catch (e: Exception) {
            throw PedidoBadRequestException(
                "Error: Eliminar Linea Pedido",
                "${e.message}"
            )
        }
    }

    @PutMapping("/{id}/lineas/{lineaId}")
    fun updateLineaPedido(
        @PathVariable id: Long,
        @PathVariable lineaId: Long,
        @RequestBody lineaPedidoDTO: LineaPedidoCreateDTO
    ): ResponseEntity<PedidoDTO> {
        val pedido = pedidosRepository.findById(id).orElseGet { throw PedidoNotFoundException(id) }
        // Buscamos la linea de pedido
        var lineaPedido = pedido.lineasPedido.find { it.id == lineaId } ?: throw LineaPedidoNotFoundException(lineaId)
        // Buscamos el producto
        var producto = productosRepository.findById(lineaPedidoDTO.productoId).orElseThrow {
            throw ProductoNotFoundException(lineaPedidoDTO.productoId)
        }

        // Lo añadimos al pedido
        lineaPedido.apply {
            cantidad = lineaPedidoDTO.cantidad
            producto = producto
            precio = producto.precio
        }
        // Salvamos el pedido y resultado
        try {
            val result = pedidosMapper.toDTO(pedidosRepository.save(pedido))
            return ResponseEntity.status(HttpStatus.OK).body(result)
        } catch (e: Exception) {
            throw PedidoBadRequestException(
                "Error: Actualizar Linea Pedido",
                "${e.message}"
            )
        }
    }

}