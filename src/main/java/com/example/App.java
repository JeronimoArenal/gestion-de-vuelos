package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {

        // Vuelo 1: Salida hoy (Punto 2 y 13)
        Vuelo v1 = Vuelo.builder()
                .destino(Destino.LONDRES)
                .precio(new BigDecimal("88.50"))
                .fechaSalida(LocalDate.of(2026, Month.MAY, LocalDate.now().getDayOfMonth()))
//                .horaSalida(LocalTime.now().plusHours(1))
                .horaSalida(LocalTime.of(15, 30))
                .fechaLlegada(LocalDate.of(2026, Month.MAY, LocalDate.now().getDayOfMonth()))
//                .horaLlegada(LocalTime.now().plusHours(3))
                .horaLlegada(LocalTime.of(23, 30))
                .numeroPlazas(3)
                .build();

        // Vuelo 2: Larga duración y cambio de día (Punto 3 y 4)
        Vuelo v2 = Vuelo.builder()
                .destino(Destino.PARIS)
                .precio(new BigDecimal("75.00"))
                .fechaSalida(LocalDate.now().plusDays(5))
                .horaSalida(LocalTime.of(10, 0))
                .fechaLlegada(LocalDate.now().plusDays(6))
                .horaLlegada(LocalTime.of(14, 0))
                .numeroPlazas(2)
                .build();

        // Vuelo 3: Fin de mes (Punto 6)
        Vuelo v3 = Vuelo.builder()
                .destino(Destino.SEVILLA)
                .precio(new BigDecimal("45.00"))
                .fechaSalida(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).minusDays(2))
                .horaSalida(LocalTime.of(16, 0))
                .fechaLlegada(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).minusDays(2))
                .horaLlegada(LocalTime.of(17, 30))
                .numeroPlazas(3)
                .build();

        List<Vuelo> todosLosVuelos = List.of(v1, v2, v3);

        // Lista de espera con 10 pasajeros
        List<Pasajero> listaEspera = new ArrayList<>(List.of(
                Pasajero.builder()
                        .nombre("Jorge Fco").primerApellido("Alborch").segundoApellido("Villar")
                        .fechaNacimiento(LocalDate.of(1990, 5, 15)).genero(Genero.MASCULINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Carolina").primerApellido("Garzón").segundoApellido("Becerra")
                        .fechaNacimiento(LocalDate.of(1985, Month.OCTOBER, 20)).genero(Genero.FEMENINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Jorge").primerApellido("Pascual").segundoApellido("Ramírez")
                        .fechaNacimiento(LocalDate.of(1984, Month.DECEMBER, 1)).genero(Genero.MASCULINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Marta").primerApellido("Rivas").segundoApellido("Cano")
                        .fechaNacimiento(LocalDate.of(1995, 3, 25)).genero(Genero.FEMENINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Rodrigo").primerApellido("Rivero").segundoApellido("Fernandez")
                        .fechaNacimiento(LocalDate.of(1978, 12, 5)).genero(Genero.MASCULINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Alberto").primerApellido("González").segundoApellido("Sánchez")
                        .fechaNacimiento(LocalDate.of(2005, 7, 14)).genero(Genero.MASCULINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Javier").primerApellido("González").segundoApellido("Sánchez")
                        .fechaNacimiento(LocalDate.of(1988, 9, 30)).genero(Genero.MASCULINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Elena").primerApellido("Blanco").segundoApellido("Gil")
                        .fechaNacimiento(LocalDate.of(1992, 2, 18)).genero(Genero.FEMENINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Duglas").primerApellido("Traydon").segundoApellido("Gonzalez")
                        .fechaNacimiento(LocalDate.of(1980, 6, 22)).genero(Genero.MASCULINO)
                        .build(),
                Pasajero.builder()
                        .nombre("Sofía").primerApellido("Castro").segundoApellido("Rey")
                        .fechaNacimiento(LocalDate.of(1998, 11, 12)).genero(Genero.FEMENINO)
                        .build()
        ));

        // Podemos añadir pasajeros a mano en cualquier vuelo pero debemos borrarlo para no volverlo a coger
        v2.getPasajeros().add(listaEspera.get(9));
        listaEspera.remove(9);

        // Invocamos al metodo de asignacion de pasajeros a los vuelos
        VueloService.asignarPasajeros(todosLosVuelos, listaEspera);

        // --- EJECUCIÓN DE VUELOSERVICE ---

        // 1. Obtener un listado de los vuelos que tienen el número de plazas completo.
        VueloService.mostrarVuelosCompletos(todosLosVuelos);
        // 2. Obtener un listado de los vuelos que tienen fecha de salida prevista para el día de hoy.
        VueloService.mostrarVuelosHoy(todosLosVuelos);
        // 3. Obtener un listado de los vuelos cuya duración sea mayor de 10 horas.
        VueloService.mostrarVuelosLargaDuracion(todosLosVuelos);
        // 4. Obtener un listado de los vuelos que pueden demorar más de un día en llegar a su destino.
        System.out.println("Ejercicio 4");
        VueloService.mostrarVuelosMasDeUnDia(todosLosVuelos);
        // 5. Obtener una colección que almacene un listado de pasajeros agrupado por el destino del vuelo.
        System.out.println("Ejercicio 5");
        VueloService.mostrarPasajerosPorDestino(todosLosVuelos);
        // 6. Crear una colección de vuelos programados para salir en los últimos 10 días del mes en curso.
        System.out.println("Ejercicio 6");
        VueloService.mostrarVuelosUltimosDiezDiasMes(todosLosVuelos);
        // 7. Crear una colección que almacene los pasajeros por el género y la edad del pasajero.
        System.out.println("Ejercicio 7");
        VueloService.mostrarPasajerosPorGeneroYEdad(todosLosVuelos);
        // 8. Mostrar la colección anterior ordenada por el nombre y los apellidos de los pasajeros en orden natural.
        System.out.println("Ejercicio 8");
        VueloService.mostrarPasajerosPorGeneroEdadOrdenados(todosLosVuelos);
        // 9. Mostrar la colección del punto 7 ordenada en orden alfabético inverso por el primer apellido,
        // sin modificar el orden natural de la clase Pasajero.
        System.out.println("Ejercicio 9");
        VueloService.mostrarPasajerosOrdenInversoApellido(todosLosVuelos);
        // 10. Obtener una colección que almacene el nombre y el apellido de los pasajeros, agrupado por las horas de duración de su viaje.
        System.out.println("Ejercicio 10");
        VueloService.mostrarPasajerosPorDuracion(todosLosVuelos);
        // 11. Mostrar el listado de pasajeros ordenado de mayor a menor por la duración del viaje.
        System.out.println("Ejercicio 11");
        VueloService.mostrarPasajerosOrdenadosPorDuracion(todosLosVuelos);

        // 13. Enviar un mensaje a los pasajeros cuyo vuelo saldrá en las próximas 3 horas.
        System.out.println("Ejercicio 13");
        List<Pasajero> inminentes = VueloService.obtenerPasajerosSalidaInminente(todosLosVuelos);
        System.out.println("Pasajeros con salida en < 3h: " + inminentes.size());

        // Punto 15
        Map<String, List<Pasajero>> porDia = VueloService.agruparPasajerosPorDiaSemana(todosLosVuelos);
        porDia.forEach((dia, pasajeros) -> System.out.println(dia + " volarán: " + pasajeros.size()));

        // Punto 16
        VueloService.mostrarVuelosFueraDeMes(todosLosVuelos);
    }

}
