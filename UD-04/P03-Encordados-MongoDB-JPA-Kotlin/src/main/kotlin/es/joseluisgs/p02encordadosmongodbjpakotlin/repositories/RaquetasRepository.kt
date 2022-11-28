package es.joseluisgs.p02encordadosmongodbjpakotlin.repositories

import es.joseluisgs.p02encordadosmongodbjpakotlin.models.Raqueta
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RaquetasRepository : MongoRepository<Raqueta, ObjectId> {
    fun findByMarca(marca: String): Raqueta?
}