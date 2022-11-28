package es.joseluisgs.kotlinspringbootrestservice.dto.pedidos

data class LineaPedidoCreateDTO(
    var cantidad: Int,
    var productoId: Long
)
