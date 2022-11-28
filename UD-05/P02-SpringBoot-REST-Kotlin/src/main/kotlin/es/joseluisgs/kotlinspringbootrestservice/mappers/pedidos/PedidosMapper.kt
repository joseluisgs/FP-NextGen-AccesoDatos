package es.joseluisgs.kotlinspringbootrestservice.mappers.pedidos

import es.joseluisgs.kotlinspringbootrestservice.dto.pedidos.PedidoDTO
import es.joseluisgs.kotlinspringbootrestservice.mappers.usuarios.UsuariosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.Pedido
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PedidosMapper
@Autowired constructor(
    private val usuariosMapper: UsuariosMapper,
    private val lineasPedidoMapper: LineasPedidoMapper,
) {
    fun toDTO(pedido: Pedido): PedidoDTO {
        return PedidoDTO(
            pedido.id,
            pedido.fecha,
            usuariosMapper.toDTO(pedido.cliente),
            pedido.total,
            lineasPedidoMapper.toDTO(pedido.lineasPedido)
        )
    }

    fun toDTO(pedidos: List<Pedido?>): List<PedidoDTO> {
        return pedidos.map { toDTO(it!!) }
    }
}