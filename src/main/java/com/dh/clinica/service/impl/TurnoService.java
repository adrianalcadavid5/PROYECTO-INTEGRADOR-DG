package com.dh.clinica.service.impl;

import com.dh.clinica.dto.request.TurnoModificarDto;
import com.dh.clinica.dto.request.TurnoRequestDto;
import com.dh.clinica.dto.response.OdontologoResponseDto;
import com.dh.clinica.dto.response.PacienteResponseDto;
import com.dh.clinica.dto.response.TurnoResponseDto;
import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.entity.Paciente;
import com.dh.clinica.entity.Turno;
import com.dh.clinica.exception.BadRequestException;
import com.dh.clinica.exception.ResourceNotFoundException;
import com.dh.clinica.repository.ITurnoRepository;
import com.dh.clinica.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {

    private final Logger logger = LoggerFactory.getLogger(TurnoService.class);
    //aca estamos haciendo inyeccion de dependencias por contructor
    private ITurnoRepository turnoRepository;
    private PacienteService pacienteService;
    private OdontologoService odontologService;
    @Autowired   //otra forma de hacer inyeccion de depencias diferente de agregarlo al constructor
    private ModelMapper modelMapper;

    public TurnoService(ITurnoRepository turnoRepository, PacienteService pacienteService, OdontologoService odontologService) {
        this.turnoRepository = turnoRepository;
        this.pacienteService = pacienteService;
        this.odontologService = odontologService;
    }

    @Override
    public TurnoResponseDto guardarTurno(TurnoRequestDto turnoRequestDto){
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoRequestDto.getPaciente_id());
        Optional<Odontologo> odontologo = odontologService.buscarPorId(turnoRequestDto.getOdontologo_id());

        if (!paciente.isPresent()) {
            throw new BadRequestException("El paciente con ID " + turnoRequestDto.getPaciente_id() + " no fue encontrado.");
        }

        if (!odontologo.isPresent()) {
            throw new BadRequestException("El odontólogo con ID " + turnoRequestDto.getOdontologo_id() + " no fue encontrado.");
        }

        // Crear y guardar el turno
        Turno turno = new Turno();
        turno.setPaciente(paciente.get());
        turno.setOdontologo(odontologo.get());
        turno.setFecha(LocalDate.parse(turnoRequestDto.getFecha()));

        Turno turnoDesdeDb = turnoRepository.save(turno);
        logger.info("Turno guardado " + turnoDesdeDb);

        // Mapear el turno guardado a TurnoResponseDto
        return mapearATurnoResponse(turnoDesdeDb);
    }

    @Override
    public Optional<TurnoResponseDto> buscarPorId(Integer id) {
        Optional<Turno> turnoDesdeDb = turnoRepository.findById(id);
        TurnoResponseDto turnoResponseDto = null;
        if (turnoDesdeDb.isPresent()){
            turnoResponseDto = mapearATurnoResponse(turnoDesdeDb.get());
            logger.info("El turno con ID " + id + " fue encontrado" + turnoDesdeDb.get());
        }else {
            logger.warn("El turno con ID "+ id + " no fue encontrado.");
        }
        return Optional.ofNullable(turnoResponseDto);
    }

    @Override
    public List<TurnoResponseDto> buscarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoResponseDto> turnoRespuesta = new ArrayList<>();
        for (Turno t: turnos){
            TurnoResponseDto turnoAuxiliar = mapearATurnoResponse(t);
            logger.info("turnos "+ t);
            turnoRespuesta.add((turnoAuxiliar));
        }
        return turnoRespuesta;
    }

    @Override
    public void modificarTurno(TurnoModificarDto turnoModificarDto) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoModificarDto.getPaciente_id());
        Optional<Odontologo> odontologo = odontologService.buscarPorId(turnoModificarDto.getOdontologo_id());
        Turno turno = null;
        if (paciente.isPresent() && odontologo.isPresent()) {
            turno = new Turno(turnoModificarDto.getId(), paciente.get(), odontologo.get(),
                    LocalDate.parse(turnoModificarDto.getFecha()));
            turnoRepository.save(turno);
            logger.info("El turno con ID " + turno.getId() + " fue modificado");
        }else {
            logger.warn("No se pudo modificar el turno. El paciente con ID " + turnoModificarDto.getPaciente_id() +
                    " o el odontólogo con ID " + turnoModificarDto.getOdontologo_id() + " no existen.");
        }
    }

    @Override
    public void eliminarTurno(Integer id) {
        Optional<Turno> turno = turnoRepository.findById(id);
        if (turno.isPresent()){
            turnoRepository.deleteById(id);
            logger.info("El turno con el ID "+ id +" fue eliminado");
        }else {
            logger.warn("No se pudo eliminar el turno con el ID " + id + " por que no fue encontrado");
            throw new ResourceNotFoundException("El turno "+ id + " no fue encontrado");
        }
    }

    @Override
    public List<Turno> buscarTurnoPaciente(String apellidoPaciente) {
        logger.info("Buscando turnos para el paciente con apellido " + apellidoPaciente);
        return turnoRepository.buscarTurnoPorApellidoPaciente(apellidoPaciente);
    }

    @Override
    public List<Turno> buscarTurnosPorFecha(String fechaStr) throws DateTimeParseException {
        logger.info("Buscando turnos para la fecha : " + fechaStr);
        //Parseamos la fecha
        LocalDate fecha = LocalDate.parse(fechaStr);
        return turnoRepository.findByFecha(fecha);
    }

    @Override
    public List<Turno> ordenarTurnosPorFecha() {
        logger.info("Ordenando turnos por fecha");
        return turnoRepository.findAllByOrderByFechaAsc();
    }

    private TurnoResponseDto convertirTurnoAResponse(Turno turnoDesdeDb){
        OdontologoResponseDto odontologoResponseDto = new OdontologoResponseDto(
                turnoDesdeDb.getOdontologo().getId(), turnoDesdeDb.getOdontologo().getNumeroMatricula(),
                turnoDesdeDb.getOdontologo().getNombre(), turnoDesdeDb.getOdontologo().getApellido()
        );
        PacienteResponseDto pacienteResponseDto = new PacienteResponseDto(turnoDesdeDb.getPaciente().getId(),
                turnoDesdeDb.getPaciente().getNombre(), turnoDesdeDb.getPaciente().getApellido(), turnoDesdeDb.getPaciente().getDni());

        TurnoResponseDto turnoARetornar = new TurnoResponseDto(turnoDesdeDb.getId(),
                pacienteResponseDto, odontologoResponseDto, turnoDesdeDb.getFecha().toString());
        return turnoARetornar;
    }

    private TurnoResponseDto mapearATurnoResponse(Turno turno){
        TurnoResponseDto turnoResponseDto = modelMapper.map(turno, TurnoResponseDto.class);
        turnoResponseDto.setOdontologoResponseDto(modelMapper.map(turno.getOdontologo(), OdontologoResponseDto.class));
        turnoResponseDto.setPacienteResponseDto(modelMapper.map(turno.getPaciente(), PacienteResponseDto.class));
        return turnoResponseDto;
    }
}
