package es.joseluisgs.p02encordadosmongodbjpa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("representantes")
public class Representante {
    private String nombre;
    private String email;

}
