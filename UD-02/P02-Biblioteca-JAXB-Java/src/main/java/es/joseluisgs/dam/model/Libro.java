package es.joseluisgs.dam.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

import java.util.UUID;

@XmlRootElement(name = "libro")
@XmlType(propOrder = {
        "autor",
        "nombre",
        "editorial",
        "isbn"
})
@Data
public class Libro {

    @XmlAttribute
    private final UUID id = UUID.randomUUID();

    private String nombre;
    private String autor;
    private String editorial;
    private String isbn;

    @XmlElement(name = "titulo")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", editorial='" + editorial + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
