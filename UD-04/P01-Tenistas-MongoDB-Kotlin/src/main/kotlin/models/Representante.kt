package models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

data class Representante(
    @BsonId
    val id: String = newId<Representante>().toString(),
    var nombre: String,
    var email: String
)