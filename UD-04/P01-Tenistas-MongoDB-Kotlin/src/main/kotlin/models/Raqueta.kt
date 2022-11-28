package models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

data class Raqueta(
    @BsonId
    val id: String = newId<Raqueta>().toString(),
    var marca: String,
    var precio: Double,
    var represetante: Representante? = null

)

// No voy a guardar para las raquetas los tenistas, porque eso lo puedo consultar de otras maneras ...
// Si no tendríamos un problema de recursividad... Ahora no lo ves, pero seguro que en Acceso a Datos lo ves!!