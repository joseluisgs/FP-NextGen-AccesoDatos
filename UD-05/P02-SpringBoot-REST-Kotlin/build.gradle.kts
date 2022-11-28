import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    // para los no-args constructors de las clases
    kotlin("plugin.jpa") version "1.6.10"
    // Para hacer Lazy Fecth que todo funcione bien, todas las clases deben ser open
    kotlin("plugin.allopen") version "1.6.10"
}

group = "es.joseluisgs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Base de datos de prueba
    runtimeOnly("com.h2database:h2")
    // Anotaciones para Jackson y manejo de la recursividad
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    implementation("javax.validation:validation-api:2.0.1.Final")
    // Lombok --> No es necesario por las ventajas de Kotlin
    // compileOnly("org.projectlombok:lombok")
    // annotationProcessor("org.projectlombok:lombok")
    // Test de spring
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Mockito Kotlin para valores nulos
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Indicamos con qué anotaciones siempre serán abietas las clases abiertas ya que usamos data classes
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}
