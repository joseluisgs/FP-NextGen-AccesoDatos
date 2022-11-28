package es.joseluisgs.dam;

import es.joseluisgs.dam.controller.BibliotecaController;
import es.joseluisgs.dam.model.Biblioteca;
import es.joseluisgs.dam.model.Libro;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String DATA_XML = System.getProperty("user.dir") + File.separator + "data" + File.separator + "biblioteca.xml";
        String DATA_XML_LUIS_VIVES = System.getProperty("user.dir") + File.separator + "data" + File.separator + "biblioteca_luis_vives.xml";

        System.out.println("Iniciamos el controlador");
        BibliotecaController controller = BibliotecaController.getInstance();
        System.out.println();

        System.out.println("*** JAXB XML *** ");

        List<Libro> libroList = new ArrayList<Libro>();

        // Creamos los libros
        Libro libro1 = new Libro();
        libro1.setIsbn("978-0134685991");
        libro1.setNombre("Effective Java");
        libro1.setAutor("Joshua Bloch");
        libro1.setEditorial("Amazon");
        libroList.add(libro1);

        Libro libro2 = new Libro();
        libro2.setIsbn("978-0596009205");
        libro2.setNombre("Head First Java, 2nd Edition");
        libro2.setAutor("Kathy Sierra");
        libro2.setEditorial("amazon");
        libroList.add(libro2);

        // Creamos la librería y le asignamos los libros
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setCentro("Acceso a Datos");
        biblioteca.setDireccion("Comunidad de Madrid");
        biblioteca.setBookList(libroList);

        try {
            // Marshalling a XML
            controller.setBookstore(biblioteca);
            System.out.println("Pasando de Objetos a XML");
            controller.printXML();
            controller.writeXMLFile(DATA_XML);
            System.out.println();

            // Unmarshallig de XML
            System.out.println("Pasando de XML a Objetos");
            biblioteca = controller.getBookstore(DATA_XML);
            System.out.println(biblioteca.toString());
            System.out.println();

            System.out.println("Todos los libros");
            biblioteca.getBooksList().forEach(System.out::println);
            System.out.println();

            System.out.println("Libros de autor que tiene la palabra sierra");
            biblioteca.getBooksList().stream().filter(libro -> libro.getAutor().toLowerCase().contains("sierra"))
                    .forEach(System.out::println);
            System.out.println();

            // incluso podríamos crearnos un fichero solo para ese bookstore de sierra
            System.out.println("Creando BookStore de Luis Vives");
            Biblioteca bibliotecaLuisVives = new Biblioteca();
            bibliotecaLuisVives.setCentro("IES Luis Vives Bookstore");
            bibliotecaLuisVives.setDireccion("Leganés");
            List<Libro> libroListAutorSierra = biblioteca.getBooksList().stream()
                    .filter(libro -> libro.getAutor().toLowerCase().contains("sierra")).collect(Collectors.toList());
            bibliotecaLuisVives.setBookList(libroListAutorSierra);
            controller.setBookstore(bibliotecaLuisVives);
            controller.printXML();
            controller.writeXMLFile(DATA_XML_LUIS_VIVES);
            System.out.println();


        } catch (JAXBException e) {
            System.err.println("ERROR:" + e.getMessage());
        }
    }
}
