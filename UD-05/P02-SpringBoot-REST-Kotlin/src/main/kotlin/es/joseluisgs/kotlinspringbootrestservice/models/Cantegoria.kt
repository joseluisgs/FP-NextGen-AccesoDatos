package es.joseluisgs.kotlinspringbootrestservice.models

import javax.persistence.*

@Entity
data class Categoria(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(unique = true)
    var nombre: String
) {
    constructor(nombre: String) : this(0, nombre)
}
