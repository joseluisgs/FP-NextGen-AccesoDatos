package es.joseluisgs.kotlinspringbootrestservice.repositories.usuarios

import es.joseluisgs.kotlinspringbootrestservice.models.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UsuariosRepository : JpaRepository<Usuario, Long> {
    fun findByUsername(username: String?): Usuario?
}