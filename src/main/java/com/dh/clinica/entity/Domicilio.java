package com.dh.clinica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  //nos da de manera automatica todos los getters * es de lombok
@Setter // nos da de manera automatica todos los setters
@AllArgsConstructor //nos crea un constructor con todos los argumentos
@NoArgsConstructor  //nos crea un constructor vacio que necesita el jackson para las serializacion y deserializacion
@Entity
@Table(name = "domicilios")
public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String calle;
    private int numero;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String localidad;
    @NotBlank(message = "El campo no puede estar en blanco")
    private String provincia;

    @Override
    public String toString() {
        return "Domicilio{" +
                "id=" + id +
                ", calle='" + calle + '\'' +
                ", numero=" + numero +
                ", localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }
}
