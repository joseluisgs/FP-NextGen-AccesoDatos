package es.joseluisgs.kotlinspringbootrestservice.controllers.categorias

import es.joseluisgs.kotlinspringbootrestservice.config.APIConfig
import es.joseluisgs.kotlinspringbootrestservice.dto.categorias.CategoriaCreateDTO
import es.joseluisgs.kotlinspringbootrestservice.dto.categorias.CategoriaProductosDTO
import es.joseluisgs.kotlinspringbootrestservice.errors.categorias.CategoriaBadRequestException
import es.joseluisgs.kotlinspringbootrestservice.errors.categorias.CategoriaNotFoundException
import es.joseluisgs.kotlinspringbootrestservice.mappers.productos.ProductosMapper
import es.joseluisgs.kotlinspringbootrestservice.models.Categoria
import es.joseluisgs.kotlinspringbootrestservice.repositories.categorias.CategoriasRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(APIConfig.API_PATH + "/categorias")
// Le aplico DI por constructor
class CategoriasRestController
@Autowired constructor(
    private val categoriasRepository: CategoriasRepository,
    private val productosMapper: ProductosMapper,
) {

    @GetMapping("")
    fun getAll(): ResponseEntity<MutableList<Categoria>> {
        val categorias = categoriasRepository.findAll()
        return ResponseEntity.ok(categorias)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Categoria> {
        val categoria = categoriasRepository.findById(id).orElseThrow { CategoriaNotFoundException(id) }
        return ResponseEntity.ok(categoria)
    }

    @PostMapping("")
    fun create(@RequestBody categoria: CategoriaCreateDTO): ResponseEntity<Categoria> {
        checkCategoriaData(categoria.nombre)
        val newCategoria = Categoria(categoria.nombre)
        try {
            val result = categoriasRepository.save(newCategoria)
            return ResponseEntity.status(HttpStatus.CREATED).body(result)
        } catch (e: Exception) {
            throw CategoriaBadRequestException(
                "Error: Insertar Categoria",
                "Campos incorrectos o nombre existente. ${e.message}"
            )
        }
    }

    @PutMapping("/{id}")
    fun update(@RequestBody categoria: CategoriaCreateDTO, @PathVariable id: Long): ResponseEntity<Categoria> {
        checkCategoriaData(categoria.nombre)
        val updateCategoria = categoriasRepository.findById(id).orElseGet { throw CategoriaNotFoundException(id) }
        updateCategoria.nombre = categoria.nombre
        try {
            return ResponseEntity.ok(categoriasRepository.save(updateCategoria))
        } catch (e: Exception) {
            throw  CategoriaBadRequestException(
                "Error: Actualizar Categoria",
                "Campos incorrectos o nombre existente. ${e.message}"
            )
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Categoria> {
        val categoria = categoriasRepository.findById(id).orElseGet { throw CategoriaNotFoundException(id) }
        val numberProductos = categoriasRepository.countByProductos(id)
        if (numberProductos > 0) {
            throw CategoriaBadRequestException(
                "Categoria con id $id",
                "Está asociadoa a $numberProductos producto(s)"
            )
        }
        try {
            categoriasRepository.delete(categoria)
            return ResponseEntity.ok(categoria)
        } catch (e: Exception) {
            throw  CategoriaBadRequestException(
                "Error: Eliminar Categoria",
                "Id de categoria inexistente o asociado a un producto. ${e.message}"
            )
        }
    }

    @GetMapping("/{id}/productos")
    fun getProductos(@PathVariable id: Long): ResponseEntity<CategoriaProductosDTO> {
        val categoria = categoriasRepository.findById(id).orElseGet { throw CategoriaNotFoundException(id) }
        val productos = categoriasRepository.findProductosByCategoria(id)
        val res = CategoriaProductosDTO(
            categoria.id,
            categoria.nombre,
            productosMapper.toDTO(productos)
        )
        try {
            return ResponseEntity.ok(res)
        } catch (e: Exception) {
            throw  CategoriaBadRequestException(
                "Error: Obtener Productos",
                "Id de categoria inexistente. ${e.message}"
            )
        }
    }

    private fun checkCategoriaData(nombre: String) {
        if (nombre.trim().isBlank()) {
            throw CategoriaBadRequestException("Nombre", "El nombre es obligatorio")
        }
    }
}
