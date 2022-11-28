package es.joseluisgs.kotlinspringbootrestservice.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// Nos permite devolver un estado cuando salta la excepción
@ResponseStatus(HttpStatus.BAD_REQUEST)
class GeneralBadRequestException(operacion: String, error: String) :
    RuntimeException("$operacion: $error")