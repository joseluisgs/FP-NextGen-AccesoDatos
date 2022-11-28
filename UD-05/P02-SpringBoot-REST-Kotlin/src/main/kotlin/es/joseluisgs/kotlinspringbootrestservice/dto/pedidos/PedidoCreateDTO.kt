package es.joseluisgs.kotlinspringbootrestservice.dto.pedidos

data class PedidoCreateDTO(
    val clienteId: Long,
    val lineasPedido: List<LineaPedidoCreateDTO>
)