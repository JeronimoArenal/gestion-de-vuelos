package com.example;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Pasajero(String nombre,
                       String primerApellido,
                       String segundoApellido,
                       LocalDate fechaNacimiento,
                       Genero genero) {

}
