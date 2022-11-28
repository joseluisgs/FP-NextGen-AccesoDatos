package es.joseluisgs.kotlinspringbootrestservice.mappers.pedidos

import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.LineaPedidoDTO
import es.joseluisgs.kotlinspringbootrestservice.mappers.productos.ProductosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.LineaPedido
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LineasPedidoMapper
@Autowired constructor(
    private val productosMapper: ProductosMapper
) {
    
    fun toDTO(lineaPedido: LineaPedido): LineaPedidoDTO {
        return LineaPedidoDTO(
            lineaPedido.id,
            lineaPedido.precio,
            lineaPedido.cantidad,
            lineaPedido.subTotal,
            productosMapper.toDTO(lineaPedido.producto)
        )
    }

    fun toDTO(lineasPedido: List<LineaPedido?>): List<LineaPedidoDTO> {
        return lineasPedido.map { toDTO(it!!) }
    }
}