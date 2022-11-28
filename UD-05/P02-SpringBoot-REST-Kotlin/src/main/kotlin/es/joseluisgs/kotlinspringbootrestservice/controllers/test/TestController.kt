package es.joseluisgs.kotlinspringbootrestservice.controllers.test

import es.joseluisgs.kotlinspringbootrestservice.config.APIConfig
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(APIConfig.API_PATH + "/test") // Definimos la url o entrada de la API REST, este caso la raíz: localhost:8080/rest/

class TestController {

    @GetMapping("")
    fun test(): String {
        return "\uD83D\uDC4B Hola desde Kotlin Springboot REST Service"
    }
}