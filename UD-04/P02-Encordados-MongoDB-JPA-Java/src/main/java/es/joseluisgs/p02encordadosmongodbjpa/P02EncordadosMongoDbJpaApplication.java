package es.joseluisgs.p02encordadosmongodbjpa;

import es.joseluisgs.p02encordadosmongodbjpa.controllers.MutuaController;
import es.joseluisgs.p02encordadosmongodbjpa.db.DataDB;
import es.joseluisgs.p02encordadosmongodbjpa.models.Raqueta;
import es.joseluisgs.p02encordadosmongodbjpa.models.Tenista;
import es.joseluisgs.p02encordadosmongodbjpa.utils.MyLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;
import java.util.stream.Collectors;

@SpringBootApplication
public class P02EncordadosMongoDbJpaApplication implements CommandLineRunner {
    @Autowired
    private MutuaController controller;

    public static void main(String[] args) {
        SpringApplication.run(P02EncordadosMongoDbJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\uD83C\uDFBE Mutua Madrid Open - Gestión de Tenistas \uD83C\uDFBE");

        // Borramos todo
        limpiarDatos();

        var represetantesInit = DataDB.getRepresentantes();

        var raquetasInit = DataDB.getRaquetasInit();

        // Añadimos a cada raqueta su representante con un bucle
        for (int i = 0; i < raquetasInit.size(); i++) {
            raquetasInit.get(i).setRepresentante(represetantesInit.get(i));
        }


        raquetasInit.forEach(controller::createRaqueta);

        // Obtenemos todas las raquetas ordenadas por marca
        System.out.println("Raquetas ordenadas por marca:");
        var raquetas = controller.getRaquetas().stream()
                .sorted(Comparator.comparing(Raqueta::getMarca)).toList();
        raquetas.forEach(System.out::println);

        // A cada tenista debemos asignarle una raqueta, porque es 1 a M
        var tenistasInit = DataDB.getTenistasInit();
        tenistasInit.get(0).setRaqueta(raquetas.get(0)); // Nadal, Babolat
        tenistasInit.get(1).setRaqueta(raquetas.get(2)); // Federer, Wilson
        tenistasInit.get(2).setRaqueta(raquetas.get(1)); // Djokovic, Head
        tenistasInit.get(3).setRaqueta(raquetas.get(0)); // Thiem, Babolat
        tenistasInit.get(4).setRaqueta(raquetas.get(0)); // Alcaraz, Babolat

        // Insertamos los tenistas
        tenistasInit.forEach(controller::createTenista);

        // Obtenemos todos los tenistas
        var tenistas = controller.getTenistas();
        tenistas.forEach(System.out::println);

        // Tenista por ID
        var tenista = controller.getTenistaById(tenistas.get(4).getId());
        // Si no es null lo imprimimos, sabemos que no lo es, pero y si no lo encuentra porque el uuid es incorrecto?
        tenista.ifPresent(System.out::println);
        // Vamos a cambiarle sus ganancias, que Carlos ha ganado el torneo
        tenista.ifPresent(t -> t.setGanancias(1000000.0 + t.getGanancias()));
        // Actualizamos el tenista
        tenista.ifPresent(controller::updateTenista);

        // vamos a buscarlo otra vez, para ver los cambios
        tenista = controller.getTenistaById(tenistas.get(4).getId());
        tenista.ifPresent(System.out::println);

        // Vamos a borrar a Roger Federer, porque se retira
        var roger = controller.getTenistaById(tenistas.get(1).getId());
        roger.ifPresent(t -> {
            controller.deleteTenista(t);
            System.out.println("Roger Federer se ha retirado");
        });


        // Sacamos todos los tenistas ordenados por ranking
        tenistas = controller.getTenistas().stream()
                .sorted(Comparator.comparing(Tenista::getRanking))
                .collect(Collectors.toList());
        tenistas.forEach(System.out::println);

        // Ademas podemos jugar con los tenistas
        // Tenista que más ha ganado
        System.out.println();
        var ganador = controller.getTenistas().stream()
                .max(Comparator.comparing(Tenista::getGanancias));
        System.out.println("Tenistas con mas ganancias: ");
        ganador.ifPresent(System.out::println);
        // Tenista más novel en el circuito
        var joven = controller.getTenistas().stream()
                .max(Comparator.comparing(Tenista::getAñoProfesional));
        System.out.println("Tenistas más novel: ");
        joven.ifPresent(System.out::println);
        // Tenista más veterano en el circuito
        var veterano = controller.getTenistas().stream()
                .min(Comparator.comparing(Tenista::getAñoProfesional));
        System.out.println("Tenistas más veterano: ");
        veterano.ifPresent(System.out::println);
        // Tenista más alto
        var alto = controller.getTenistas().stream()
                .max(Comparator.comparing(Tenista::getAltura));
        System.out.println("Tenistas más alto: ");
        alto.ifPresent(System.out::println);
        // Agrupamos por nacionalidad
        System.out.println("Tenistas agrupados por nacionalidad: ");
        var nacionalidades = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getPais));
        nacionalidades.forEach((pais, tenistasPais) -> {
            System.out.println(pais + ": " + tenistasPais);
        });
        // Agrupamos por mano hábil
        System.out.println("Tenistas agrupados por mano hábil: ");
        var manos = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getManoDominante));
        manos.forEach((mano, tenistasMano) -> {
            System.out.println(mano + ": " + tenistasMano);
        });
        // ¿Cuantos tenistas a un o dos manos hay?
        System.out.println("Nº de tenistas por mano hábil: ");
        var manoDominante = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getManoDominante));
        manoDominante.forEach((mano, tenistasMano) -> {
            System.out.println(mano + ": " + tenistasMano.size());
        });

        // ¿Cuantos tenistas hay por cada raqueta?
        System.out.println("Nº de tenistas por raqueta: ");
        var raquetasTenistas = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getRaqueta));
        raquetasTenistas.forEach((raqueta, tenistasRaqueta) -> {
            System.out.println(raqueta.getMarca() + ": " + tenistasRaqueta.size());
        });

        // La raqueta más cara
        System.out.println("Raqueta más cara: ");
        var raquetaMasCara = controller.getRaquetas().stream()
                .max(Comparator.comparing(Raqueta::getPrecio));
        raquetaMasCara.ifPresent(System.out::println);
        // ¿Qué tenista usa la raqueta más cara?
        System.out.println("Tenista que usa la raqueta más cara: ");
        var tenistaRaquetaMasCara = controller.getTenistas().stream()
                .filter(t -> t.getRaqueta().equals(
                        controller.getRaquetas().stream()
                                .max(Comparator.comparing(Raqueta::getPrecio))
                                .get()
                ))
                .findFirst();
        tenistaRaquetaMasCara.ifPresent(System.out::println);
        // Ganancias totales de todos los tenistas
        System.out.println("Ganancias totales de todos los tenistas: ");
        var gananciasTotales = controller.getTenistas().stream()
                .mapToDouble(Tenista::getGanancias)
                .sum();
        System.out.println(MyLocale.toLocalMoney(gananciasTotales));
        // Precio medio de las raquetas
        System.out.println("Precio medio de las raquetas: ");
        var precioMedioRaquetas = controller.getRaquetas().stream()
                .mapToDouble(Raqueta::getPrecio)
                .average();
        precioMedioRaquetas.ifPresent(p -> System.out.println(MyLocale.toLocalMoney(p)));

    }

    private void limpiarDatos() {
        System.out.println("Limpiando datos...");
        controller.raquetasDeleteAll();
        controller.tenistasDeleteAll();

    }
}
