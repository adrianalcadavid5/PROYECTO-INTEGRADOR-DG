package com.dh.clinica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "odontologos")
public class Odontologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String numeroMatricula;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String nombre;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String apellido;

    @OneToMany(mappedBy = "odontologo")
    @JsonManagedReference(value = "odontologo-turno")
    //@JsonIgnore
    public Set<Turno> turnoSet;



    @Override
    public String toString() {
        return "Odontologo{" +
                "id=" + id +
                ", numeroMatricula='" + numeroMatricula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                '}';
    }
}
