package es.joseluisgs.kotlinspringbootrestservice.repositories.productos

import es.joseluisgs.kotlinspringbootrestservice.models.Producto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


// Extendemos de JPA que nos ofrece operaciones CRUD y el uso de páginas filtros y ordenaciones
@Repository
interface ProductosRepository : JpaRepository<Producto, Long> {

    // Podemos crear nuestros métodos específicos fuera de los típicos CRUD
    fun findByNombreContainsIgnoreCase(nombre: String?, pageable: Pageable?): Page<Producto?>

    @Query("select count(l.id) from LineaPedido l WHERE l.producto.id = :id")
    fun countByLineasPedido(id: Long): Long

}