package es.joseluisgs.p02encordadosmongodbjpa.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Objects;
import java.util.Set;

@Document("raquetas")
public class Raqueta {
    @Id
    private ObjectId id;
    private String marca;
    private Double precio;
    // Mi lista de tenistas 1-M y hacemos referencia a la colección de tenistas por el id almacenando
    // gracias a @DocumentReference y lookup
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'tenistas':?#{#self._id} }")
    private Set<Tenista> tenistas;

    // Mi representante, es un documento embebido
    private Representante representante;

    public Raqueta(String marca, Double precio) {
        this.marca = marca;
        this.precio = precio;
    }

    public Raqueta() {
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Set<Tenista> getTenistas() {
        return tenistas;
    }

    public void setTenistas(Set<Tenista> tenistas) {
        this.tenistas = tenistas;
    }

    // Metodo helpers
    public void addTenista(Tenista tenista) {
        //tenistas.add(tenista);
        tenista.setRaqueta(this);
    }

    public Representante getRepresentante() {
        return representante;
    }

    public void setRepresentante(Representante representante) {
        this.representante = representante;
    }

    public void removeTenista(Tenista tenista) {
        tenista.removeRaqueta();
    }

    @Override
    public String toString() {
        return "Raqueta{" +
                "id=" + id +
                ", marca=" + marca +
                ", precio=" + precio +
                ", representante=" + representante +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Raqueta raqueta)) return false;
        return id.equals(raqueta.id) && marca.equals(raqueta.marca) && precio.equals(raqueta.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marca, precio);
    }
}