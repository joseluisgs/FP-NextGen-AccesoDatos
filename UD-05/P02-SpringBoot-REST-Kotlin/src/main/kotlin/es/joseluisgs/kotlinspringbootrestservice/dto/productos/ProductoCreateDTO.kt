package es.joseluisgs.kotlinspringbootrestservice.dto.productos

data class ProductoCreateDTO(
    val nombre: String,
    val precio: Double,
    val categoriaId: Long
)