package es.joseluisgs.p02encordadosmongodbjpa.models;

import es.joseluisgs.p02encordadosmongodbjpa.enums.ManoDominante;
import es.joseluisgs.p02encordadosmongodbjpa.enums.ManoDominanteUtils;
import es.joseluisgs.p02encordadosmongodbjpa.enums.TipoReves;
import es.joseluisgs.p02encordadosmongodbjpa.enums.TipoRevesUtils;
import es.joseluisgs.p02encordadosmongodbjpa.utils.MyLocale;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.Objects;


@Document("tenistas")
public class Tenista {
    @Id
    private ObjectId id;
    private String nombre;
    private Integer ranking;
    private LocalDate fechaNacimiento;
    private Integer añoProfesional;
    private Integer altura;
    private Integer peso;
    private Double ganancias;
    private ManoDominante manoDominante;
    private TipoReves tipoReves;
    private Integer puntos;
    private String pais;
    // Mi referencia a la raqueta

    // Apuntamos a la raqueta por su id en modo referencia M-1
    //@DocumentReference(lazy = true)
    @DocumentReference
    private Raqueta raqueta;

    public Tenista(String nombre, Integer ranking, LocalDate fechaNacimiento, Integer añoProfesional, Integer altura,
                   Integer peso, Double ganancias, ManoDominante manoDominante, TipoReves tipoReves,
                   Integer puntos, String pais) {
        this.nombre = nombre;
        this.ranking = ranking;
        this.fechaNacimiento = fechaNacimiento;
        this.añoProfesional = añoProfesional;
        this.altura = altura;
        this.peso = peso;
        this.ganancias = ganancias;
        this.manoDominante = manoDominante;
        this.tipoReves = tipoReves;
        this.puntos = puntos;
        this.pais = pais;
    }

    public Tenista() {
    }

    public ObjectId getId() {
        return id;
    }

    private void setId(ObjectId id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getAñoProfesional() {
        return añoProfesional;
    }

    public void setAñoProfesional(Integer añoProfesional) {
        this.añoProfesional = añoProfesional;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Double getGanancias() {
        return ganancias;
    }

    public void setGanancias(Double ganancias) {
        this.ganancias = ganancias;
    }

    public ManoDominante getManoDominante() {
        return manoDominante;
    }

    public void setManoDominante(ManoDominante manoDominante) {
        this.manoDominante = manoDominante;
    }

    public TipoReves getTipoReves() {
        return tipoReves;
    }

    public void setTipoReves(TipoReves tipoReves) {
        this.tipoReves = tipoReves;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }


    public Raqueta getRaqueta() {
        return raqueta;
    }

    public void setRaqueta(Raqueta raqueta) {
        this.raqueta = raqueta;
    }


    public void removeRaqueta() {
        this.raqueta.getTenistas().remove(this);
        this.raqueta = null;
    }

    @Override
    public String toString() {
        return "Tenista{" +
                "uuid=" + id +
                ", nombre='" + nombre + '\'' +
                ", ranking=" + ranking +
                ", fechaNacimiento=" + MyLocale.toLocalDate(fechaNacimiento) +
                ", añoProfesional=" + añoProfesional +
                ", altura=" + altura +
                ", peso=" + peso +
                ", ganancias=" + MyLocale.toLocalMoney(ganancias) +
                ", manoDominante=" + ManoDominanteUtils.toString(manoDominante) +
                ", tipoReves=" + TipoRevesUtils.toString(tipoReves) +
                ", puntos=" + puntos +
                ", pais='" + pais + '\'' +
                ", raqueta=" + raqueta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tenista tenista)) return false;
        return id.equals(tenista.id) && nombre.equals(tenista.nombre) && ranking.equals(tenista.ranking) && fechaNacimiento.equals(tenista.fechaNacimiento) && añoProfesional.equals(tenista.añoProfesional) && altura.equals(tenista.altura) && peso.equals(tenista.peso) && ganancias.equals(tenista.ganancias) && manoDominante == tenista.manoDominante && tipoReves == tenista.tipoReves && puntos.equals(tenista.puntos) && pais.equals(tenista.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, ranking, fechaNacimiento, añoProfesional, altura, peso, ganancias, manoDominante, tipoReves, puntos, pais);
    }
}
