package es.joseluisgs.kotlinspringbootrestservice.errors.usuarios

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UsuarioNotFoundException(id: Long) :
    RuntimeException("No se puede encontrar usuario con ID: $id")
