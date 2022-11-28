package es.joseluisgs.kotlinspringbootrestservice.dto.pedidos

import es.joseluisgs.kotlinspringbootrestservice.dto.usuarios.UsuarioPedidoDTO
import java.time.LocalDateTime

data class PedidoDTO(
    val id: Long,
    val fecha: LocalDateTime,
    val cliente: UsuarioPedidoDTO,
    val total: Double,
    val lineasPedido: List<LineaPedidoDTO>
)