package com.dh.clinica;

import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.service.impl.OdontologoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class OdontologoServiceTest {
    @Autowired
    OdontologoService odontologoService;

    Odontologo odontologo;
    Odontologo odontologoDesdeDb;

    @BeforeEach
    void crearOdontologo(){
        odontologo = new Odontologo();
        odontologo.setNumeroMatricula("1234");
        odontologo.setNombre("Pedro");
        odontologo.setApellido("Perez");
        odontologoDesdeDb = odontologoService.guardarOdontologo(odontologo);
    }

    @Test
    @DisplayName("Testear que un odontologo se guarde en la base de datos")
    void caso1(){
        //dado
        //cuando
        //entonces
        assertNotNull(odontologoDesdeDb.getId());
        assertEquals("1234", odontologoDesdeDb.getNumeroMatricula());
        assertEquals("Pedro", odontologoDesdeDb.getNombre());
        assertEquals("Perez", odontologoDesdeDb.getApellido());
    }
}