package es.joseluisgs.kotlinspringbootrestservice.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Pedido(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @CreatedDate
    val fecha: LocalDateTime,

    // Mi relaciones

    // Un pedido tiene un usuario, pero un usuario tiene muchos pedidos, unidireccional P->U
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    var cliente: Usuario,

    // Un pedido tiene muchas lineas de pedido P -> LP (Bidreccional)
    @OneToMany(mappedBy = "pedido", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference // para romper la recursividad usamos @JsonManagedReference
    val lineasPedido: MutableList<LineaPedido> = mutableListOf()
) {
    // En vez de una función creo una propiedad claculada, es decir cuando quieran adquirir el getter
    val total: Double
        get() = lineasPedido.sumOf { it.subTotal }

    // Helper para manejar la recursividad
    fun addLineaPedido(lineaPedido: LineaPedido) {
        lineasPedido.add(lineaPedido)
        lineaPedido.pedido = this
    }

    fun removeLineaPedido(lineaPedido: LineaPedido) {
        lineasPedido.remove(lineaPedido)
        lineaPedido.pedido = null
    }

    constructor(cliente: Usuario) : this(0, LocalDateTime.now(), cliente, mutableListOf())
}