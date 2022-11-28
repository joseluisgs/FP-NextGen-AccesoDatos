package es.joseluisgs.kotlinspringbootrestservice.dto.pedidos

import es.joseluisgs.kotlinspringbootrestservice.config.APIConfig
import java.time.LocalDateTime
import javax.validation.constraints.Min

data class PedidoListDTO(
    val consulta: String = LocalDateTime.now().toString(),
    val project: String = APIConfig.PROJECT_NAME,
    val version: String = APIConfig.API_VERSION,
    val data: List<PedidoDTO>,
    @Min(message = "El número de página debe ser mayor o igual a 0", value = 0)
    val currentPage: Int = 0,
    @Min(message = "El número de elementos por página debe ser mayor o igual a 1", value = 1)
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val sort: String? = null,
    val links: String? = null
)