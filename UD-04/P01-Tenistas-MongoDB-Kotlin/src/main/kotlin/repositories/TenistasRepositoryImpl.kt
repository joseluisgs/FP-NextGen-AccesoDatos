package repositories

import db.MongoDbManager
import models.Tenista
import mu.KotlinLogging
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl : TenistasRepository {
    override fun findAll(): List<Tenista> {
        return MongoDbManager.database.getCollection<Tenista>().find().toList()
    }

    override fun findById(id: String): Tenista? {
        logger.debug { "findById($id)" }
        return MongoDbManager.database.getCollection<Tenista>().findOneById(id)
    }

    override fun save(entity: Tenista): Tenista {
        logger.debug { "save($entity)" }
        MongoDbManager.database.getCollection<Tenista>().save(entity)
        return entity
    }

    private fun insert(entity: Tenista): Tenista {
        logger.debug { "save($entity) - creando" }
        MongoDbManager.database.getCollection<Tenista>().save(entity)
        return entity
    }

    private fun update(entity: Tenista): Tenista {
        logger.debug { "save($entity) - actualizando" }
        MongoDbManager.database.getCollection<Tenista>().save(entity)
        return entity
    }

    override fun delete(entity: Tenista): Boolean {
        logger.debug { "delete($entity)" }
        return MongoDbManager.database.getCollection<Tenista>().deleteOneById(entity.id).wasAcknowledged()
    }
}