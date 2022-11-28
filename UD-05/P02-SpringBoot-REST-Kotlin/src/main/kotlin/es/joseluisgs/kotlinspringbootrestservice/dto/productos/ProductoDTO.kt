package es.joseluisgs.kotlinspringbootrestservice.dto.productos

import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank


data class ProductoDTO(
    val id: Long,
    @NotBlank(message = "El nombre no puede estar vacío")
    val nombre: String,
    @Min(message = "El precio no puede ser negativo", value = 0)
    val precio: Double,
    val imagen: String?,
    val createdAt: LocalDateTime,
    val slug: String,
    val categoria: String

)