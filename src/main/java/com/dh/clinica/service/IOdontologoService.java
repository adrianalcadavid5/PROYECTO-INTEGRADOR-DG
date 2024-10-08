package com.dh.clinica.service;

import com.dh.clinica.entity.Odontologo;

import java.util.List;
import java.util.Optional;

public interface IOdontologoService {
    Odontologo guardarOdontologo(Odontologo odontologo);

    Optional<Odontologo> buscarPorId(Integer id);


    List<Odontologo> buscarTodos();

    void modificarOdontologo(Odontologo odontologo);
    void eliminarOdontologo(Integer id);

    List<Odontologo> buscarPorApellidoyNombre(String apellildo, String nombre);

    List<Odontologo> buscarPorNombreOApelido(String nombre,String apellido);

    List<Odontologo> buscarPorNombre(String nombre);
    List<Odontologo> buscarPorApellido(String apellido);
}
