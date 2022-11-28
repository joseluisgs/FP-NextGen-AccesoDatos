package es.joseluisgs.p02encordadosmongodbjpakotlin.controllers

import es.joseluisgs.p02encordadosmongodbjpakotlin.exceptions.RaquetaException
import es.joseluisgs.p02encordadosmongodbjpakotlin.models.Raqueta
import es.joseluisgs.p02encordadosmongodbjpakotlin.models.Tenista
import es.joseluisgs.p02encordadosmongodbjpakotlin.repositories.RaquetasRepository
import es.joseluisgs.p02encordadosmongodbjpakotlin.repositories.TenistasRepository
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

val logger = KotlinLogging.logger {}

// Le inyectamos las dependencias
@Controller
class MutuaController
@Autowired constructor(
    private val raquetasRepository: RaquetasRepository,
    private val tenistasRepository: TenistasRepository
) {

    // TENISTAS

    // Tenistas
    fun tenistasDeleteAll() {
        logger.info("Borrando todos los tenistas")
        tenistasRepository.deleteAll()
    }

    fun getTenistas(): List<Tenista> {
        logger.info("Obteniendo Tenistas")
        return tenistasRepository.findAll()
    }

    fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        tenistasRepository.save(tenista)
        return tenista
    }

    fun getTenistaById(id: ObjectId): Tenista? {
        logger.debug { "Obteniendo tenista con uuid $id" }
        return tenistasRepository.findById(id).orElse(null)
        // Mejor lanzar una excepción aqui si es null .orElseThrow { RaquetaException("No existe la raqueta con id $id") }
    }

    fun updateTenista(tenista: Tenista) {
        logger.debug { "Updating tenista $tenista" }
        tenistasRepository.save(tenista)
    }

    fun deleteTenista(tenista: Tenista): Boolean {
        logger.debug { "Borrando tenista $tenista" }
        tenistasRepository.delete(tenista)
        return true
    }

    // RAQUETAS
    fun raquetasDeleteAll() {
        logger.info("Borrando todas los raquetas")
        raquetasRepository.deleteAll()
    }

    fun getRaquetas(): List<Raqueta> {
        logger.info("Obteniendo Raquetas")
        return raquetasRepository.findAll()
    }

    fun createRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Creando raqueta $raqueta" }
        raquetasRepository.save(raqueta)
        return raqueta
    }

    fun getRaquetaById(id: ObjectId): Raqueta? {
        logger.debug { "Obteniendo raqueta con uuid $id" }
        return raquetasRepository.findById(id).orElse(null)
    }

    fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository.save(raqueta)
    }

    fun deleteRaqueta(raqueta: Raqueta): Boolean {
        logger.debug { "Borrando raqueta $raqueta" }
        raquetasRepository.delete(raqueta)
        return true
    }

    fun findRaquetaByMarca(marca: String): Raqueta {
        logger.debug { "Buscando raqueta por marca $marca" }
        return raquetasRepository.findByMarca(marca)
            ?: throw RaquetaException("No se ha encontrado la raqueta con marca $marca")
    }
}