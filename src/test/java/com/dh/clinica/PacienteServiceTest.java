package com.dh.clinica;

import com.dh.clinica.entity.Domicilio;
import com.dh.clinica.entity.Paciente;
import com.dh.clinica.service.impl.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //con esta utilizamos la base de datos en memoria
@Transactional //sirve para limpiar la base de datos cuando hacemos test para que no se cargue nada de lo que probamos, como un rollback
class PacienteServiceTest {
    @Autowired
    PacienteService pacienteService;

    Paciente paciente;
    Paciente pacienteDesdeDb;

    @BeforeEach //quiere decir que se cargue antes de cada test
    void crearPaciente(){
        Domicilio domicilio = new Domicilio(null,"Falsa",456,"Cipolleti", "Rio Negro");
        paciente = new Paciente();
        paciente.setApellido("Romero");
        paciente.setNombre("Luciana");
        paciente.setDni("56699655");
        paciente.setFechaIngreso(LocalDate.of(2024,7,16));
        paciente.setDomicilio(domicilio);
        pacienteDesdeDb = pacienteService.guardarPaciente(paciente);
    }
    @Test
    @DisplayName("Testear que un paciente se guarde en la base de datos con su domicilio correctamente")
    void caso1(){
        //dato
        //cuando
        //entonces
        assertNotNull(pacienteDesdeDb.getId());
    }

    @Test
    @DisplayName("Testear que un paciente pueda ser obtenido cuando se envia el id con exito")
    void caso2(){
        //dado
        Integer id  = pacienteDesdeDb.getId();
        //cuaando
        Paciente pacienteEncontrado = pacienteService.buscarPorId(id).get();
        //entonces
        assertEquals(id, pacienteEncontrado.getId());
    }
}