package es.joseluisgs.kotlinspringbootrestservice.repositories.pedidos

import es.joseluisgs.kotlinspringbootrestservice.models.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PedidosRepository : JpaRepository<Pedido, Long>
