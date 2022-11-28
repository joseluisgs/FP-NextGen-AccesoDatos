package es.joseluisgs.p02encordadosmongodbjpa.repositories;

import es.joseluisgs.p02encordadosmongodbjpa.models.Tenista;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenistasRepository extends MongoRepository<Tenista, ObjectId> {
    Tenista findByNombre(String nombre);
}