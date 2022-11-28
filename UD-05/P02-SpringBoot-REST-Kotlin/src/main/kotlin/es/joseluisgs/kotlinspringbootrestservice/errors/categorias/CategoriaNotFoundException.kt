package es.joseluisgs.kotlinspringbootrestservice.errors.categorias

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.NOT_FOUND)
class CategoriaNotFoundException(id: Long) :
    RuntimeException("No se puede encontrar la categoria con ID: $id")