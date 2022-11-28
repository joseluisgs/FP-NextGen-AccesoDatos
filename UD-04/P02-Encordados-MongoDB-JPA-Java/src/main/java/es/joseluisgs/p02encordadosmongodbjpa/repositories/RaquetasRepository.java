package es.joseluisgs.p02encordadosmongodbjpa.repositories;

import es.joseluisgs.p02encordadosmongodbjpa.models.Raqueta;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaquetasRepository extends MongoRepository<Raqueta, ObjectId> {
    Raqueta findByMarca(String marca);
}