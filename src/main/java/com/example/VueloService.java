package com.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class VueloService {

    //Asignar pasajeros - El Repartidor
    public static void asignarPasajeros(List<Vuelo> vuelos, List<Pasajero> espera) {
        int indexPasajero = 0;      //Primer pasajero en subir

        //Con for cambiamos de avion. Con while cargamos el avion
        for (Vuelo vuelo : vuelos) {        //Para cada instancia (objeto) de tipo vuelo de la llista de vuellos. Haz
            // Mientras la cantidad de pasajeros < que nº plazas Y indexPasajro < total lista espera
            while (vuelo.getPasajeros().size() < vuelo.getNumeroPlazas() && indexPasajero < espera.size()) {
                vuelo.getPasajeros().add(espera.get(indexPasajero));
                indexPasajero++;
            }
        }
    }

    // 1. Obtener un listado de los vuelos que tienen el número de plazas completo.
    public static void mostrarVuelosCompletos(List<Vuelo> todosLosVuelos) {
        System.out.println("=== VUELOS CON PLAZAS COMPLETAS ===");
        todosLosVuelos.stream()
                .filter(v -> v.getPasajeros().size() >= v.getNumeroPlazas())
                .forEach(v -> System.out.println("Vuelo destino " + v.getDestino() + " está completo."));
    }

    // 2. Obtener un listado de los vuelos que tienen fecha de salida prevista para el día de hoy.
    public static void mostrarVuelosHoy(List<Vuelo> todosLosVuelos) {
        List<Vuelo> vuelosHoy = todosLosVuelos.stream()
                .filter(v -> v.getFechaSalida().equals(LocalDate.now()))
                .toList();
        if (vuelosHoy.isEmpty()) {
            System.out.println("No hay vuelos programados para hoy.");
        } else {
            vuelosHoy.forEach(v -> System.out.println("Vuelo a " + v.getDestino() +
                    " | Salida: " + v.getHoraSalida()));
        }
    }

    // 3. Obtener un listado de los vuelos cuya duración sea mayor de 10 horas.
    // Hemos usado atTime para combinar fecha y hora en un solo objeto (LocalDateTime), lo que permite que ChronoUnit.HOURS
    // calcule correctamente la diferencia aunque el vuelo cambie de día como ocurre en el vuelo 2.
    public static void mostrarVuelosLargaDuracion(List<Vuelo> todosLosVuelos) {
        todosLosVuelos.stream()
                .filter(v -> {
                    long horas = ChronoUnit.HOURS.between(
                            v.getFechaSalida().atTime(v.getHoraSalida()),
                            v.getFechaLlegada().atTime(v.getHoraLlegada())
                    );
                    return horas > 10;
                })
                .forEach(v -> System.out.println("Vuelo de larga duración a: " + v.getDestino()));
    }

    // 4. Obtener un listado de los vuelos que pueden demorar más de un día en llegar a su destino.
    public static void mostrarVuelosMasDeUnDia(List<Vuelo> todosLosVuelos) {
        todosLosVuelos.stream()
                .filter(v -> ChronoUnit.DAYS.between(v.getFechaSalida(), v.getFechaLlegada()) >= 1)
                .forEach(v -> System.out.println("Vuelo con llegada en fecha distinta: " + v.getDestino()));
    }

    // 5. Obtener una colección que almacene un listado de pasajeros agrupado por el destino del vuelo.
    public static void mostrarPasajerosPorDestino(List<Vuelo> todosLosVuelos) {
        Map<Destino, List<Pasajero>> agrupados = todosLosVuelos.stream()
                .collect(Collectors.groupingBy(
                        Vuelo::getDestino,
                        //Con flatMapping sacamos a los pasajeros del vuelo y llos metemos en una nueva lista
                        Collectors.flatMapping(v -> v.getPasajeros().stream(), Collectors.toList())
                ));

        // Impresión de los resultados
        agrupados.forEach((destino, lista) -> {
            System.out.println("Destino " + destino + " tiene " + lista.size() + " pasajeros.");
        });
    }

    // 6. Crear una colección de vuelos programados para salir en los últimos 10 días del mes en curso.
    public static void mostrarVuelosUltimosDiezDiasMes(List<Vuelo> todosLosVuelos) {
        LocalDate hoy = LocalDate.now();
        LocalDate ultimoDiaMes = hoy.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate haceDiezDiasDelFin = ultimoDiaMes.minusDays(10);

        todosLosVuelos.stream()
                .filter(v -> v.getFechaSalida().getMonth().equals(hoy.getMonth()) &&
                        v.getFechaSalida().getYear() == hoy.getYear()) // Mes actual
                .filter(v -> v.getFechaSalida().isAfter(haceDiezDiasDelFin) ||
                        v.getFechaSalida().isEqual(ultimoDiaMes)) // Últimos 10 días
                .forEach(v -> System.out.println("Vuelos en los 10 ultimos días de mes a: " + v.getDestino()));
    }

    // 7. Crear una colección que almacene los pasajeros por el género y la edad del pasajero.
    public static void mostrarPasajerosPorGeneroYEdad(List<Vuelo> todosLosVuelos) {
        // Primero aplanamos todos los pasajeros de todos los vuelos en un solo stream
        Map<Genero, Map<Integer, List<Pasajero>>> agrupados = todosLosVuelos.stream()
                .flatMap(v -> v.getPasajeros().stream())
                .collect(Collectors.groupingBy(
                        Pasajero::genero, // Primera agrupación: Género
                        Collectors.groupingBy(p -> {
                            // Cálculo de edad comparando nacimiento con la fecha actual
                            return Period.between(p.fechaNacimiento(), LocalDate.now()).getYears();
                        })
                ));

        // Imprimimos
        agrupados.forEach((genero, edades) -> {
            System.out.println("Género: " + genero);
            edades.forEach((edad, lista) -> {
                System.out.println("  Edad " + edad + ": " + lista.size() + " pasajeros");
            });
        });
    }

    // 8. Mostrar la colección anterior ordenada por el nombre y los apellidos de los pasajeros en orden natural.
    public static void mostrarPasajerosPorGeneroEdadOrdenados(List<Vuelo> todosLosVuelos) {
        // 1. Agrupamos (Punto 7) usando TreeMap para que el Género salga ordenado (F antes que M)
        Map<Genero, Map<Integer, List<Pasajero>>> agrupados = todosLosVuelos.stream()
                .flatMap(v -> v.getPasajeros().stream())
                .collect(Collectors.groupingBy(
                        Pasajero::genero,
                        TreeMap::new, // Ordena por Género
                        Collectors.groupingBy(
                                p -> Period.between(p.fechaNacimiento(), LocalDate.now()).getYears(),
                                TreeMap::new, // Ordena por Edad
                                Collectors.toList()
                        )
                ));

        // 2. Imprimimos y ordenamos los pasajeros (Punto 8)
        agrupados.forEach((genero, edades) -> {
            System.out.println("\nGénero: " + genero);
            edades.forEach((edad, lista) -> {
                System.out.println("  Edad " + edad + ":");
                lista.stream()
                        .sorted(Comparator.comparing(Pasajero::nombre)
                                .thenComparing(Pasajero::primerApellido)
                                .thenComparing(Pasajero::segundoApellido))
                        .forEach(p -> System.out.println("    - " + p.nombre() + " " + p.primerApellido()));
            });
        });
    }

    // 9. Mostrar la colección del punto 7 ordenada por primer apellido en orden inverso.
    public static void mostrarPasajerosOrdenInversoApellido(List<Vuelo> todosLosVuelos) {
        // Reutilizamos la lógica de agrupación del punto 7
        Map<Genero, Map<Integer, List<Pasajero>>> agrupados = todosLosVuelos.stream()
                .flatMap(v -> v.getPasajeros().stream())
                .collect(Collectors.groupingBy(
                        Pasajero::genero,
                        Collectors.groupingBy(p -> Period.between(p.fechaNacimiento(), LocalDate.now()).getYears())
                ));

        // Imprimimos
        agrupados.forEach((genero, edades) -> {
            System.out.println("\nGénero: " + genero);
            edades.forEach((edad, lista) -> {
                System.out.println("  Edad " + edad + ":");
                lista.stream()
                        .sorted(Comparator.comparing(Pasajero::primerApellido).reversed()) // Orden inverso por primer apellido
                        .forEach(p -> System.out.println("    - " + p.primerApellido() + ", " + p.nombre()));
            });
        });
    }

    // 10. Obtener una colección que almacene el nombre y el apellido de los pasajeros, agrupado por las horas de duración de su viaje.
    public static Map<Long, List<String>> agruparNombresPorDuracion(List<Vuelo> todosLosVuelos) {
        return todosLosVuelos.stream()
                .collect(Collectors.groupingBy(v -> {
                    // Combinamos fecha y hora para un cálculo exacto
                    return ChronoUnit.HOURS.between(
                            v.getFechaSalida().atTime(v.getHoraSalida()),
                            v.getFechaLlegada().atTime(v.getHoraLlegada())
                    );
                }, Collectors.flatMapping(v -> v.getPasajeros().stream()
                                .map(p -> p.nombre() + " " + p.primerApellido()),
                        Collectors.toList())));
    }

    // imprimir
    public static void mostrarPasajerosPorDuracion(List<Vuelo> todosLosVuelos) {
        Map<Long, List<String>> mapa = agruparNombresPorDuracion(todosLosVuelos);

        mapa.forEach((horas, nombres) -> {
            System.out.println("Vuelos de " + horas + " horas de duración:");
            nombres.forEach(n -> System.out.println(" " + n));
        });
    }

    // 11. Mostrar el listado de pasajeros ordenado de mayor a menor por la duración del viaje.
    public static void mostrarPasajerosOrdenadosPorDuracion(List<Vuelo> todosLosVuelos) {
        todosLosVuelos.stream()
                // 1. Ordenamos los vuelos por duración de forma descendente
                .sorted((v1, v2) -> {
                    long duracion1 = ChronoUnit.HOURS.between(v1.getFechaSalida().atTime(v1.getHoraSalida()), v1.getFechaLlegada().atTime(v1.getHoraLlegada()));
                    long duracion2 = ChronoUnit.HOURS.between(v2.getFechaSalida().atTime(v2.getHoraSalida()), v2.getFechaLlegada().atTime(v2.getHoraLlegada()));
                    return Long.compare(duracion2, duracion1); // v2 vs v1 para que sea descendente
                })
                // 2. Por cada vuelo ya ordenado, imprimimos su duración y sus pasajeros
                .forEach(v -> {
                    long horas = ChronoUnit.HOURS.between(v.getFechaSalida().atTime(v.getHoraSalida()), v.getFechaLlegada().atTime(v.getHoraLlegada()));
                    System.out.println("Duración: " + horas + " horas | Destino: " + v.getDestino());
                    v.getPasajeros().forEach(p -> System.out.println("   - " + p.nombre() + " " + p.primerApellido()));
                });
    }

    // 13. Enviar un mensaje a los pasajeros cuyo vuelo saldrá en las próximas 3 horas.
    public static List<Pasajero> obtenerPasajerosSalidaInminente(List<Vuelo> todosLosVuelos) {
        return todosLosVuelos.stream()
                .filter(v -> v.getFechaSalida().equals(LocalDate.now()))
                .filter(v -> {
                    long diff = ChronoUnit.HOURS.between(LocalTime.now(), v.getHoraSalida());
                    return diff >= 0 && diff < 3;
                })
                .flatMap(v -> v.getPasajeros().stream())
                .toList();
    }

    // 15. Crear una colección que almacene el listado de pasajeros agrupado por el día en que tiene lugar su vuelo,
    // considerando que el vuelo tiene lugar en el mes en curso. Al mostrar la colección resultante, mostrar el nombre del día
    //de la semana en español.
    public static Map<String, List<Pasajero>> agruparPasajerosPorDiaSemana(List<Vuelo> todosLosVuelos) {
        Locale espanol = new Locale("es", "ES");
        return todosLosVuelos.stream()
                .filter(v -> v.getFechaSalida().getMonth() == LocalDate.now().getMonth())
                .collect(Collectors.groupingBy(v -> v.getFechaSalida()
                                .getDayOfWeek()
                                .getDisplayName(TextStyle.FULL, espanol),
                        Collectors.flatMapping(v -> v.getPasajeros().stream(), Collectors.toList())));
    }

    // 16. Crear una colección de los vuelos que no están previstos para el mes en curso
    // y mostrar el nombre del mes... en español.
    public static void mostrarVuelosFueraDeMes(List<Vuelo> todosLosVuelos) {
        Locale espanol = new Locale("es", "ES");
        todosLosVuelos.stream()
                .filter(v -> !v.getFechaSalida().getMonth().equals(LocalDate.now().getMonth()))
                .forEach(v -> {
                    String nombreMes = v.getFechaSalida().getMonth()
                            .getDisplayName(TextStyle.FULL, espanol);
                    System.out.println("Vuelo a " + v.getDestino() + " previsto para " + nombreMes);
                });
    }
}
