package es.joseluisgs.kotlinspringbootrestservice.models

import es.joseluisgs.kotlinspringbootrestservice.Extensions.toSlug
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Producto(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @NotBlank(message = "Nombre no puede estar vacío")
    var nombre: String,
    @Min(value = 0, message = "Precio no puede ser negativo")
    var precio: Double,
    var imagen: String?,
    @CreatedDate
    val createdAt: LocalDateTime,


    // Mis relaciones
    // Un producto tiene una categoria, una categoria tiene muchos productos, unidireccional P -> C
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    var categoria: Categoria
) {
    constructor(nombre: String, precio: Double, categoria: Categoria) :
            this(0, nombre, precio, null, LocalDateTime.now(), categoria)

    val slug: String
        get() = nombre.toSlug()
}
