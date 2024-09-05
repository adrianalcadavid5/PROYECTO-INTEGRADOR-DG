package com.dh.clinica.repository;

import com.dh.clinica.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOdontologoRepository extends JpaRepository<Odontologo, Integer> {

    List<Odontologo> findByApellidoAndNombre(String apellido, String nombre);

    @Query("SELECT o FROM Odontologo o WHERE o.nombre = ?1 OR o.apellido = ?2")
    List<Odontologo> buscarPorNombreOApellido(String nombre, String apellido);
    List<Odontologo> findByNombre(String nombre);
    List<Odontologo> findByApellido(String apellido);

}
