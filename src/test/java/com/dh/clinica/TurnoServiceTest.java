package com.dh.clinica;
import com.dh.clinica.dto.request.TurnoRequestDto;
import com.dh.clinica.dto.response.TurnoResponseDto;
import com.dh.clinica.entity.Domicilio;
import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.entity.Paciente;
import com.dh.clinica.service.impl.OdontologoService;
import com.dh.clinica.service.impl.PacienteService;
import com.dh.clinica.service.impl.TurnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class TurnoServiceTest {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;

    private Paciente paciente;
    private Odontologo odontologo;
    private TurnoRequestDto turnoRequestDto;

    TurnoResponseDto turno;
    @BeforeEach
    void setUp(){
        //creamos y guardamos primero el paciente
        paciente = new Paciente();
        paciente.setApellido("Gomez");
        paciente.setNombre("Juan");
        paciente.setDni("98777888");
        paciente.setFechaIngreso(LocalDate.of(2024,7,16));
        paciente.setDomicilio(new Domicilio(null,"Cordoba",499,"Cipolleti", "Rio Negro"));
        paciente = pacienteService.guardarPaciente(paciente);

        //creamos y guardamos un odontologo
        odontologo = new Odontologo();
        odontologo.setNumeroMatricula("1778");
        odontologo.setNombre("Manuel");
        odontologo.setApellido("Muelas");
        odontologo = odontologoService.guardarOdontologo(odontologo);

        //creamos el DTO turno
        TurnoRequestDto turnoRequestDto = new TurnoRequestDto();
        turnoRequestDto.setPaciente_id(paciente.getId());
        turnoRequestDto.setOdontologo_id(odontologo.getId());
        turnoRequestDto.setFecha("2024-09-15");

        //Guardamos el turno usando el DTO
        turno = turnoService.guardarTurno(turnoRequestDto);
    }

    @Test
    @DisplayName("Testear que un turno de guarde en la base de datos")
    void caso1(){
        //dado
        //cuando
        //entonces
        assertNotNull(turno.getId(), "El ID del turno no debería ser nulo");
        assertEquals(paciente.getId(), turno.getPacienteResponseDto().getId(), "El ID del paciente asociado al turno no coinciden");
        assertEquals(odontologo.getId(), turno.getPacienteResponseDto().getId(), "El ID del odontólogo asociado al turno no coincide");

    }
}
