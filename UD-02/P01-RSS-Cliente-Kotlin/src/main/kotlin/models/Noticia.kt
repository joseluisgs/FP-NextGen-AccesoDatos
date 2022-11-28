package models

import java.io.Serializable

data class Noticia(
    var titulo: String = "",
    var link: String = "",
    var descripcion: String = "",
    var contenido: String = "",
    var fecha: String = "",
    var imagen: String = ""
) : Serializable
