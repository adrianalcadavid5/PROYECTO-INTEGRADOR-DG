package com.dh.clinica;

import com.dh.clinica.dao.impl.DomicilioDaoH2;
import com.dh.clinica.dao.impl.OdontologoH2Dao;
import com.dh.clinica.db.H2Connection;
import com.dh.clinica.model.Odontologo;
import com.dh.clinica.service.OdontologoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTest {
    public static final Logger logger = LoggerFactory.getLogger(OdontologoServiceTest.class);

    OdontologoService odontologoService = new OdontologoService(new OdontologoH2Dao());

    @BeforeAll
    static void tablas(){
        H2Connection.crearTablas();
    }

    @Test
    @DisplayName("Testear que un odondologo se guarde en la base de datos")
    void caso1(){
        // dado
        Odontologo odontologo = new Odontologo("1234", "Valeria", "Serna");
        //cuando
        Odontologo odontologoDesdeDB = odontologoService.guardarOdontologo(odontologo);
        //enconces
        assertNotNull(odontologoDesdeDB.getId());
    }

    @Test
    @DisplayName("Testear que un odontologo puede ser encontrado cuando se envia un id")
    void  caso2(){
        //dado
        Integer id = 1;
        //cuando
        Odontologo odontologo = odontologoService.buscarPorId(id);
        //enconces
        assertEquals(id, odontologo.getId());
    }
    @Test
    @DisplayName("Testear que busque todos los odontologos")
    void  caso3(){
        Odontologo odontologo1 = new Odontologo("122","pepe","perez");
        Odontologo odontologo2 = new Odontologo("552","ana","fernandez");
        odontologoService.guardarOdontologo(odontologo1);
        odontologoService.guardarOdontologo(odontologo2);
        odontologoService.buscarTodos();
    }

}