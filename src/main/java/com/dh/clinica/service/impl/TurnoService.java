package com.dh.clinica.service.impl;

import com.dh.clinica.dto.request.TurnoModificarDto;
import com.dh.clinica.dto.request.TurnoRequestDto;
import com.dh.clinica.dto.response.OdontologoResponseDto;
import com.dh.clinica.dto.response.PacienteResponseDto;
import com.dh.clinica.dto.response.TurnoResponseDto;
import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.entity.Paciente;
import com.dh.clinica.entity.Turno;
import com.dh.clinica.repository.ITurnoRepository;
import com.dh.clinica.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {
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
        Turno turno = new Turno();
        Turno turnoDesdeDb = null;
        TurnoResponseDto turnoARetornar = null;

        if (paciente.isPresent() && odontologo.isPresent()) {
            //mapear el turnoRequestDto a turno
            turno.setPaciente(paciente.get());
            turno.setOdontologo(odontologo.get());
            turno.setFecha(LocalDate.parse(turnoRequestDto.getFecha()));
            //voy a persistir el turno.
            turnoDesdeDb = turnoRepository.save(turno);

            //mapear el turnoDesdeDb a turnoResponseDto, hecho a mano
            //turnoARetornar = convertirTurnoaResponse(turnoDesdeDb);

            //turno mapeado con modelmapper
            turnoARetornar = mapearATurnoResponse(turnoDesdeDb);
        }
        return turnoARetornar;
    }

    @Override
    public Optional<TurnoResponseDto> buscarPorId(Integer id) {
        Optional<Turno> turnoDesdeDb = turnoRepository.findById(id);
        TurnoResponseDto turnoResponseDto = null;
        if (turnoDesdeDb.isPresent()){
            turnoResponseDto = mapearATurnoResponse(turnoDesdeDb.get());
        }
        return Optional.ofNullable(turnoResponseDto);
    }

    @Override
    public List<TurnoResponseDto> buscarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoResponseDto> turnoRespuesta = new ArrayList<>();
        for (Turno t: turnos){
            TurnoResponseDto turnoAuxiliar = mapearATurnoResponse(t);
            turnoRespuesta.add((turnoAuxiliar));
        }
        return turnoRespuesta;
    }

    @Override
    public void modificarTurno(TurnoModificarDto turnoModificarDto) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoModificarDto.getPaciente_id());
        Optional<Odontologo> odontologo = odontologService.buscarPorId(turnoModificarDto.getOdontologo_id());
        Turno turno = null;
        if (paciente.isEmpty() && odontologo.isPresent()) {
            turno = new Turno(turnoModificarDto.getId(), paciente.get(), odontologo.get(),
                    LocalDate.parse(turnoModificarDto.getFecha()));
            turnoRepository.save(turno);
        }
    }

    @Override
    public void eliminarTurno(Integer id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public List<Turno> buscarTurnoPaciente(String apellidoPaciente) {
        return turnoRepository.buscarTurnoPorApellidoPaciente(apellidoPaciente);
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
