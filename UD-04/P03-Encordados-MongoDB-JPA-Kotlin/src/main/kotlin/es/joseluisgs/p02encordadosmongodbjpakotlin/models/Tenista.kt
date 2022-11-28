package es.joseluisgs.p02encordadosmongodbjpakotlin.models

import es.joseluisgs.p02encordadosmongodbjpakotlin.extensions.toLocalDate
import es.joseluisgs.p02encordadosmongodbjpakotlin.extensions.toLocalMoney
import es.joseluisgs.p02encordadosmongodbjpakotlin.extensions.toLocalNumber
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDate
import java.util.*


@Document("tenistas")
data class Tenista(
    @Id
    val id: ObjectId = ObjectId.get(),
    var nombre: String,
    var ranking: Int,
    var fechaNacimiento: LocalDate,
    var añoProfesional: Int,
    var altura: Int,
    var peso: Int,
    var ganancias: Double,
    var manoDominante: ManoDominante,
    var tipoReves: TipoReves,
    var puntos: Int,
    var pais: String,

    // Mi referencia a la raqueta
    // Apuntamos a la raqueta por su id en modo referencia M-1
    @DocumentReference()
    //@DocumentReference(lookup = "{ '_id' : ?#{#target} }") // Otra forma de ponerlos
    var raqueta: Raqueta? = null

) {

    override fun toString(): String {
        return "Tenista(uuid=$id, nombre='$nombre', ranking=$ranking, " +
                "fechaNacimiento=${fechaNacimiento.toLocalDate()}, " +
                "añoProfesional=$añoProfesional, " +
                "altura=${(altura.toDouble() / 100).toLocalNumber()} cm, " +
                "peso=$peso Kg, " +
                "ganancias=${ganancias.toLocalMoney()}, " +
                "manoDominante=${manoDominante.mano}, " +
                "tipoReves=${tipoReves.tipo}, " +
                "puntos=$puntos, pais=$pais, " +
                // Cuidado con la recursividad, porque al imprimir la raqueta, esta a su vez imprime el tenista y
                // así sucesivamente entramos en un bucle infinito
                "raqueta=${raqueta})"
    }

    // ENUMS de la propia clase
    enum class ManoDominante(val mano: String) {
        DERECHA("DERECHA"),
        IZQUIERDA("IZQUIERDA");

        companion object {
            fun from(manoDominante: String): ManoDominante {
                return when (manoDominante.uppercase()) {
                    "DERECHA" -> DERECHA
                    "IZQUIERDA" -> IZQUIERDA
                    else -> throw IllegalArgumentException("ManoDominante no reconocida")
                }
            }
        }
    }

    enum class TipoReves(val tipo: String) {
        UNA_MANO("UNA MANO"),
        DOS_MANOS("DOS MANOS");

        companion object {
            fun from(tipoReves: String): TipoReves {
                return when (tipoReves.uppercase()) {
                    "UNA MANO" -> UNA_MANO
                    "DOS MANOS" -> DOS_MANOS
                    else -> throw IllegalArgumentException("TipoReves no reconocida")
                }
            }
        }
    }


}