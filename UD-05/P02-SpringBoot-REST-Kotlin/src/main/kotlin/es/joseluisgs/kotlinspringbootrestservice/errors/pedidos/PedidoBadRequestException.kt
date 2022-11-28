package es.joseluisgs.kotlinspringbootrestservice.errors.pedidos

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class PedidoBadRequestException(campo: String, error: String) :
    RuntimeException("Error: $campo: $error")