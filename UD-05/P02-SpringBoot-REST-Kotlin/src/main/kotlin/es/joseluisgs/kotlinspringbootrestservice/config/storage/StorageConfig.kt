package es.joseluisgs.kotlinspringbootrestservice.config.storage

import es.joseluisgs.kotlinspringbootrestservice.services.storage.StorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfig {
    // Inicializamos el sistema de ficheros
    @Bean
    fun init(storageService: StorageService): CommandLineRunner {
        return CommandLineRunner { args: Array<String> ->
            // Inicializamos el servicio de ficheros
            storageService.deleteAll() // Borramos todos (esto deberíamos quitarlo)
            storageService.init() // inicializamos
        }
    }
}