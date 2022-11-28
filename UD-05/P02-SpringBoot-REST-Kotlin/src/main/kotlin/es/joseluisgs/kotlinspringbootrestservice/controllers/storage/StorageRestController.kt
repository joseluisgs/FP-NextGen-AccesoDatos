package es.joseluisgs.kotlinspringbootrestservice.controllers.storage

import es.joseluisgs.kotlinspringbootrestservice.config.APIConfig
import es.joseluisgs.kotlinspringbootrestservice.errors.storage.StorageException
import es.joseluisgs.kotlinspringbootrestservice.services.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(APIConfig.API_PATH + "/storage")
class StorageRestController
@Autowired constructor(
    private val storageService: StorageService
) {
    @GetMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun serveFile(
        @PathVariable filename: String?,
        request: HttpServletRequest
    )
            : ResponseEntity<Resource> {
        val file: Resource = storageService.loadAsResource(filename.toString())
        var contentType: String? = null
        contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (ex: IOException) {
            throw StorageException("No se puede determinar el tipo del fichero", ex)
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }

    @PostMapping(
        value = [""],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadFile(
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Map<String, String>> {

        return try {
            if (!file.isEmpty) {
                val fileStored = storageService.store(file)
                val urlStored = storageService.getUrl(fileStored)
                val response = mapOf("url" to urlStored)
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            } else {
                throw StorageException("No se puede subir un fichero vacío")
            }
        } catch (e: StorageException) {
            throw StorageException("No se puede subir un fichero vacío")
        }
    }

    // Implementar el resto de metodos del servicio que nos interesen...
    // Delete file, listar ficheros, etc....
}
