package es.joseluisgs.p02encordadosmongodbjpakotlin.models

import org.springframework.data.mongodb.core.mapping.Document


@Document("representantes")
data class Representante(
    var nombre: String,
    var email: String,
)
