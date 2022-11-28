package controllers

import models.Tenista
import mu.KotlinLogging
import repositories.TenistasRepository

private val logger = KotlinLogging.logger {}

class MutuaController(
    private val tenistasRepository: TenistasRepository
) {

    // TENISTAS

    fun getTenistas(): List<Tenista> {
        logger.info("Obteniendo Tenistas")
        return tenistasRepository.findAll()
    }

    fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        tenistasRepository.save(tenista)
        return tenista
    }

    fun getTenistaById(id: String): Tenista? {
        logger.debug { "Obteniendo tenista con id $id" }
        return tenistasRepository.findById(id)
    }

    fun updateTenista(tenista: Tenista) {
        logger.debug { "Updating tenista $tenista" }
        tenistasRepository.save(tenista)
    }

    fun deleteTenista(it: Tenista): Boolean {
        logger.debug { "Borrando tenista $it" }
        return tenistasRepository.delete(it)
    }
}