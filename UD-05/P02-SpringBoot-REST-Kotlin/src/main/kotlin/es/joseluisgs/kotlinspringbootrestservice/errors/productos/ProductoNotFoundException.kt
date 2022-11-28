package es.joseluisgs.kotlinspringbootrestservice.errors.productos

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.NOT_FOUND)
class ProductoNotFoundException(id: Long) :
    RuntimeException("No se puede encontrar el producto ID: $id")