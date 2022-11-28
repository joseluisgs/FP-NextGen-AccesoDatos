package es.joseluisgs.kotlinspringbootrestservice.mappers.usuarios

import es.joseluisgs.kotlinspringbootrestservice.dto.usuarios.UsuarioPedidoDTO
import es.joseluisgs.kotlinspringbootrestservice.models.Usuario
import org.springframework.stereotype.Component

@Component
class UsuariosMapper {
    fun toDTO(usuario: Usuario): UsuarioPedidoDTO {
        return UsuarioPedidoDTO(
            usuario.username,
            usuario.fullName,
            usuario.email
        )
    }
}