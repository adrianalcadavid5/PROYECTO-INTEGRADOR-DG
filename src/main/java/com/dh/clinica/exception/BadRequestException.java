package com.dh.clinica.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}

/*
BadRequestException:

Esta excepción se utiliza cuando el cliente envía una solicitud con parámetros incorrectos o inválidos. Por ejemplo, si intentas crear un
turno pero los datos de entrada son incorrectos (como un ID de odontólogo o paciente que no existe en la base de datos), podrías lanzar
esta excepción.
Cuándo usarla:
Cuando los datos de la solicitud no cumplen con las reglas del negocio o están incompletos. Por ejemplo, si intentas crear un turno para
un odontólogo o paciente que no existe, lanzarías esta excepción para indicar que la solicitud es inválida.

En conclusion:
BadRequestException: Se lanza cuando la solicitud del cliente contiene datos incorrectos o inválidos.
 */