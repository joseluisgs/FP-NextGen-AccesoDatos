package es.joseluisgs.kotlinspringbootrestservice.errors.categorias

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// Nos permite devolver un estado cuando salta la excepción
@ResponseStatus(HttpStatus.BAD_REQUEST)
class CategoriaBadRequestException(campo: String, error: String) :
    RuntimeException("Error: $campo: $error")