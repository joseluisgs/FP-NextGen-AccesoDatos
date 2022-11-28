package es.joseluisgs.kotlinspringbootrestservice.models

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
data class LineaPedido(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    var precio: Double,
    var cantidad: Int,

    // Mi relaciones

    // Relación muchos a uno. Una linea de pedido tiene un producto y un producto tiene una linea de pedido
    // LP -> Producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    val producto: Producto,

    // Una Línea de pedido pertenece a un pedido Bidireccional LP -> P
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference // Evitamos recursividad
    var pedido: Pedido?
) {
    constructor(id: Long, precio: Double, cantidad: Int, producto: Producto) : this(
        id,
        precio,
        cantidad,
        producto,
        null
    )

    constructor(precio: Double, cantidad: Int, producto: Producto) : this(
        0,
        precio,
        cantidad,
        producto,
        null
    )

    /// En vez de una función creo una propiedad claculada, es decir cuando quieran adquirir el getter
    val subTotal
        get() = this.precio * this.cantidad
}