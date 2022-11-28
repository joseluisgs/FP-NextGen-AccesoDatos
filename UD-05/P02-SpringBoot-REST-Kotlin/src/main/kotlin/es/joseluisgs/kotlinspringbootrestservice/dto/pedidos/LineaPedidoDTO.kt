package es.joseluisgs.kotlinspringbootrestservice.dto.pedidos

import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO

data class LineaPedidoDTO(
    val id: Long,
    val precio: Double,
    val cantidad: Int,
    val subTotal: Double,
    val producto: ProductoDTO,
)