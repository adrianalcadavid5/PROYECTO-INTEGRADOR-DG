package com.dh.clinica.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "turnos")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // para generar el id auto incremental
    private Integer id;
    @ManyToOne
    //@JsonManagedReference(value = "paciente-turno")
    private Paciente paciente;
    @ManyToOne
    //@JsonManagedReference(value = "odontologo-turno")  //para mostrar
    private Odontologo odontologo;
    private LocalDate fecha;



    @Override
    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", paciente=" + paciente +
                ", odontologo=" + odontologo +
                ", fecha=" + fecha +
                '}';
    }
}
