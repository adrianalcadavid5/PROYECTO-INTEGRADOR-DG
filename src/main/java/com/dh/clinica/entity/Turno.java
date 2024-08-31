package com.dh.clinica.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference(value = "paciente-turno")//En una relacion ManyToOne no se recomienda ningun tipo de cascade
    private Paciente paciente;
    @ManyToOne
    @JsonBackReference(value = "odontologo-turno")  //para mostrar
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
