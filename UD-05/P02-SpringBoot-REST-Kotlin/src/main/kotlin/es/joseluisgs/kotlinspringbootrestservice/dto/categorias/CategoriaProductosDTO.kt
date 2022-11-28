package es.joseluisgs.kotlinspringbootrestservice.dto.categorias

import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO

class CategoriaProductosDTO(
    var id: Long,
    var nombre: String,
    var productos: List<ProductoDTO>
)
