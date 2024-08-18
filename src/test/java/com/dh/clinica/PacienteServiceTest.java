package com.dh.clinica;

import com.dh.clinica.dao.impl.DomicilioDaoH2;
import com.dh.clinica.dao.impl.PacienteDaoH2;
import com.dh.clinica.db.H2Connection;
import com.dh.clinica.model.Domicilio;
import com.dh.clinica.model.Paciente;
import com.dh.clinica.service.PacienteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacienteServiceTest {
    public static final Logger logger = LoggerFactory.getLogger(PacienteServiceTest.class);

    //llamo al pacienteSevice y lo vinculo con el pacienteDaoH2 para usarlo en el test
    PacienteService pacienteService = new PacienteService(new PacienteDaoH2());

    @BeforeAll
    static void tablas(){
        H2Connection.crearTablas();
    }


    @Test
    @DisplayName("Testear que un paciente se guarde en la base de datos con su domicilio")
    void caso1(){
        //dado ...... un paciente, este debe de ir con el constructor sin el id
        Paciente paciente = new Paciente("Romero","Luciana", "56655", LocalDate.of(2024,7,16),
                new Domicilio("falsa", 456, "Cipolleti", "Rio Negro"));
        //cuando  .. llamamos a nuestro servicio PacienteService y hago arriba
        Paciente pacienteDesdeDB = pacienteService.guardarPaciente(paciente);
        //entonces   -- probamos si el paciente no tiene id no se guarda en la base de datos
        assertNotNull(pacienteDesdeDB.getId());

    }
    @Test
    @DisplayName("Testear que un paciente pueda ser obtenido cuando se envia un id")
    void caso2(){
       //dado   .. id 1 se que esta guardado en la base datos
        Integer id = 1;
       //cuando
        Paciente paciente = pacienteService.buscarPorId(id);
       //enconces
        assertEquals(id, paciente.getId());
    }
}