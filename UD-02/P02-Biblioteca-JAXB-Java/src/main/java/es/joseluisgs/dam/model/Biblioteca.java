package es.joseluisgs.dam.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.UUID;

@XmlRootElement(namespace = "es.joseluisgs.dam.model")
public class Biblioteca {
    @XmlAttribute
    private final UUID id = UUID.randomUUID();

    @XmlElementWrapper(name = "libros")
    @XmlElement(name = "libro")
    private List<Libro> libros;
    
    private String centro;
    private String direccion;

    public void setBookList(List<Libro> libroList) {
        this.libros = libroList;
    }

    public List<Libro> getBooksList() {
        return libros;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Biblioteca{" +
                "centro='" + centro + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}