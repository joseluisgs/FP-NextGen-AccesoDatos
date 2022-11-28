package es.joseluisgs.p02encordadosmongodbjpakotlin.repositories

import es.joseluisgs.p02encordadosmongodbjpakotlin.models.Tenista
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TenistasRepository : MongoRepository<Tenista, ObjectId> {
    fun findByNombre(nombre: String): Tenista?
}