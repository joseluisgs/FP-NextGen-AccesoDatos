package es.joseluisgs.p02encordadosmongodbjpakotlin.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Document("raquetas")
data class Raqueta(
    @Id
    val id: ObjectId = ObjectId.get(),
    var marca: String,
    var precio: Double,
    var representante: Representante? = null,
) {
    // Mi lista de tenistas 1-M y hacemos referencia a la colección de tenistas por el id almacenando
    // gracias a @DocumentReference y lookup
    @ReadOnlyProperty
    // @Field("tenistas")
    // Coleeción de tenistas estará almacenado nuestro id
    // Estará la info en los tenistas de la raqueta...
    @DocumentReference(lookup = "{'tenistas':?#{#self._id} }")
    var tenistas: MutableSet<Tenista>? = null

}