package es.joseluisgs.kotlinspringbootrestservice.mappers.productos

import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.productos.ProductoDTO
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import org.springframework.stereotype.Component

@Component
class ProductosMapper {
    fun toDTO(producto: Producto): ProductoDTO {
        return ProductoDTO(
            producto.id,
            producto.nombre,
            producto.precio,
            producto.imagen,
            producto.createdAt,
            producto.slug,
            producto.categoria.nombre
        )
    }

    fun toDTO(productos: List<Producto?>): List<ProductoDTO> {
        return productos.map { toDTO(it!!) }
    }

    fun fromDTO(productoDTO: ProductoCreateDTO, categoria: Categoria): Producto {
        return Producto(
            productoDTO.nombre,
            productoDTO.precio,
            categoria,
        )
    }
}