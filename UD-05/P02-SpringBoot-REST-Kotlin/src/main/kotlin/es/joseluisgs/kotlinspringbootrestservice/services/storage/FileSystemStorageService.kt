package es.joseluisgs.kotlinspringbootrestservice.services.storage

import es.joseluisgs.kotlinspringbootrestservice.controllers.storage.StorageRestController
import es.joseluisgs.kotlinspringbootrestservice.errors.storage.StorageException
import es.joseluisgs.kotlinspringbootrestservice.errors.storage.StorageFileNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream


@Service
class FileSystemStorageService(
    @Value("\${upload.root-location}") path: String
) : StorageService {
    // Directorio raiz de nuestro almacén de ficheros
    private val rootLocation: Path

    // Inicializador
    init {
        rootLocation = Paths.get(path)
    }

    override fun store(file: MultipartFile): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val justFilename = filename.replace(".$extension", "")
        val storedFilename = System.currentTimeMillis().toString() + "_" + justFilename + "." + extension
        try {
            if (file.isEmpty) {
                throw StorageException("Fallo al almacenar un fichero vacío $filename")
            }
            if (filename.contains("..")) {
                // This is a security check
                throw StorageException("No se puede almacenar un fichero fuera del path permitido $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedFilename
            }
        } catch (e: IOException) {
            throw StorageException("Fallo al almacenar fichero $filename", e)
        }
    }

    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootLocation, 1)
                .filter { path -> !path.equals(rootLocation) }
                .map(rootLocation::relativize)
        } catch (e: IOException) {
            throw StorageException("Fallo al leer los ficheros almacenados", e)
        }
    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        return try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                    "No se puede leer fichero: $filename"
                )
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("No se puede leer fichero: $filename", e)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }


    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("No se puede inicializar el sistema de almacenamiento", e)
        }
    }

    override fun delete(filename: String) {
        val justFilename: String = StringUtils.getFilename(filename).toString()
        try {
            val file = load(justFilename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            throw StorageException("Error al eliminar un fichero", e)
        }
    }

    override fun getUrl(filename: String): String {
        return MvcUriComponentsBuilder // El segundo argumento es necesario solo cuando queremos obtener la imagen
            // En este caso tan solo necesitamos obtener la URL
            .fromMethodName(StorageRestController::class.java, "serveFile", filename, null)
            .build().toUriString()
    }
}