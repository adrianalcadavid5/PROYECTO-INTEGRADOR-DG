package com.dh.clinica.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String apellido;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String nombre;
    @NotBlank(message = "El campo no puede estar en blanco")
    @Size(min = 7, max = 15)
    private String dni;
    @NotNull(message = "El campo no puede ser nulo")
    private LocalDate fechaIngreso;

    @Valid //se anota aca cuando hay una relacion unidireccional, se le debe de informar que debe de validar el domicilio,
    @OneToOne(cascade = CascadeType.ALL) //alll por que si elimino a un paciente que se elimine el dommicilio tambien. ya no lo necesitaria
    @JoinColumn(name = "id_domicilio") //le indico como quiero que se llame la clave foránea
    private Domicilio domicilio;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE)//si necesito eliminar el turno, no se elimine el paciente.
    @JsonManagedReference(value = "paciente-turno")
    //@JsonIgnore
    public Set<Turno> turnoSet;


    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", apellido='" + apellido + '\'' +
                ", nombre='" + nombre + '\'' +
                ", dni='" + dni + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", domicilio=" + domicilio +
                '}';
    }
}
