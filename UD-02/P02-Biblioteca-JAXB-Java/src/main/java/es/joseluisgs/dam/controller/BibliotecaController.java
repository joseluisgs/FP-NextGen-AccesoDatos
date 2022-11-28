package es.joseluisgs.dam.controller;

import es.joseluisgs.dam.model.Biblioteca;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class BibliotecaController {
    private static BibliotecaController instance;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private Biblioteca biblioteca;

    private BibliotecaController() {
    }

    /**
     * Devuelve la instancia del controlador
     *
     * @return
     */
    public static BibliotecaController getInstance() {
        if (instance == null) {
            instance = new BibliotecaController();
        }
        return instance;
    }

    /**
     * Convierte una lista de objetos en XML
     *
     * @param biblioteca
     * @throws JAXBException
     */
    private void convertObjectToXML(Biblioteca biblioteca) throws JAXBException {
        this.biblioteca = biblioteca;
        // Creamos el contexto
        JAXBContext context = JAXBContext.newInstance(Biblioteca.class);
        // Marshall --> Object to XML
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    }

    /**
     * Carla los datos de Objetos y los trasforma en XML
     *
     * @param biblioteca
     * @throws JAXBException
     */
    public void setBookstore(Biblioteca biblioteca) throws JAXBException {
        convertObjectToXML(biblioteca);
    }

    /**
     * Escribe un fichero XML con el contenido de los datos
     *
     * @param uri
     * @throws JAXBException
     */
    public void writeXMLFile(String uri) throws JAXBException {
        this.marshaller.marshal(biblioteca, new File(uri));
        System.out.println("Fichero XML generado con éxito");
    }

    /**
     * Muestra por pantalla el contenido de nuestros datos
     *
     * @throws JAXBException
     */
    public void printXML() throws JAXBException {
        this.marshaller.marshal(biblioteca, System.out);
    }

    private Biblioteca convertXMLToObject(String uri) throws JAXBException {
        // Creamos el contexto
        JAXBContext context = JAXBContext.newInstance(Biblioteca.class);
        this.unmarshaller = context.createUnmarshaller();
        // Unmarshall --> XML toObject
        this.biblioteca = (Biblioteca) this.unmarshaller.unmarshal(new File(uri));
        return this.biblioteca;
    }

    /**
     * Carlos los datos de un XML y los transforma en Objetos
     *
     * @param uri
     * @return
     * @throws JAXBException
     */
    public Biblioteca getBookstore(String uri) throws JAXBException {
        return convertXMLToObject(uri);
    }
}
