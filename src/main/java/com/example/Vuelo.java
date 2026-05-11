package com.example;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class Vuelo {
    private Destino destino;
    private BigDecimal precio;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private LocalDate fechaLlegada;
    private LocalTime horaLlegada;
    private int numeroPlazas;
    @Builder.Default        //Evitamos NullPointerException y Set siempre existe aunque este vacio
    private Set<Pasajero> pasajeros = new HashSet<>();

}
