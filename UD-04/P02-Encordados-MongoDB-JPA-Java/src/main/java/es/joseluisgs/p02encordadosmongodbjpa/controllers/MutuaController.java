package es.joseluisgs.p02encordadosmongodbjpa.controllers;

import es.joseluisgs.p02encordadosmongodbjpa.models.Raqueta;
import es.joseluisgs.p02encordadosmongodbjpa.models.Tenista;
import es.joseluisgs.p02encordadosmongodbjpa.repositories.RaquetasRepository;
import es.joseluisgs.p02encordadosmongodbjpa.repositories.TenistasRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class MutuaController {
    private final Logger logger = Logger.getLogger(MutuaController.class.getName());

    // Mis dependencias
    private final RaquetasRepository raquetasRepository;
    private final TenistasRepository tenistasRepository;

    @Autowired
    public MutuaController(RaquetasRepository raquetasRepository, TenistasRepository tenistasRepository) {
        this.raquetasRepository = raquetasRepository;
        this.tenistasRepository = tenistasRepository;
    }

    // Tenistas
    public void tenistasDeleteAll() {
        logger.info("Borrando todos los tenistas");
        tenistasRepository.deleteAll();
    }

    public List<Tenista> getTenistas() {
        logger.info("Obteniendo Tenistas");
        return tenistasRepository.findAll();
    }

    public Tenista createTenista(Tenista tenista) {
        logger.info("Creando Tenista");
        return tenistasRepository.save(tenista);
    }

    public Optional<Tenista> getTenistaById(ObjectId id) {
        logger.info("Obteniendo Tenista con id: " + id);
        return tenistasRepository.findById(id);
    }

    public Tenista updateTenista(Tenista tenista) {
        logger.info("Actualizando Tenista con uuid: " + tenista.getId());
        return tenistasRepository.save(tenista);
    }

    public void deleteTenista(Tenista tenista) {
        logger.info("Eliminando Tenista con id: " + tenista.getId());
        tenistasRepository.delete(tenista);
    }

    // Raquetas
    public void raquetasDeleteAll() {
        logger.info("Borrando todas los raquetas");
        raquetasRepository.deleteAll();
    }

    public List<Raqueta> getRaquetas() {
        logger.info("Obteniendo Raquetas");
        return raquetasRepository.findAll();
    }

    public Raqueta createRaqueta(Raqueta raqueta) {
        logger.info("Creando Raqueta");
        return raquetasRepository.save(raqueta);
    }

    public Optional<Raqueta> getRaquetaById(ObjectId id) {
        logger.info("Obteniendo Raqueta con id: " + id);
        return raquetasRepository.findById(id);
    }

    public Raqueta updateRaqueta(Raqueta raqueta) {
        logger.info("Actualizando Raqueta con uuid: " + raqueta.getId());
        return raquetasRepository.save(raqueta);
    }

    public void deleteRaqueta(Raqueta raqueta) {
        logger.info("Eliminando Raqueta con uuid: " + raqueta.getId());
        raquetasRepository.delete(raqueta);
    }
}
