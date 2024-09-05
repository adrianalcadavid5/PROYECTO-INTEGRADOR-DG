package com.dh.clinica.service.impl;

import com.dh.clinica.entity.Paciente;
import com.dh.clinica.exception.BadRequestException;
import com.dh.clinica.exception.ResourceNotFoundException;
import com.dh.clinica.repository.IPacienteRepository;
import com.dh.clinica.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {
    private final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    private IPacienteRepository pacienteRepository;

    public PacienteService(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente guardarPaciente(Paciente paciente) {
        //valido que el apellido, nombre, dni, fechaIngreso y domicilio no sean nulo o esten vacias
        if (paciente.getApellido() == null || paciente.getApellido().isEmpty()){
            throw new BadRequestException("El apellido del paciente no puede estar vacio");
        }
        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()){
            throw new BadRequestException("El nombre del paciente no puede estar vacio");
        }
        if (paciente.getDni() == null || paciente.getDni().isEmpty()){
            throw new BadRequestException("El DNI del paciente no puede estar vacio");
        }
        if (paciente.getFechaIngreso() == null){
            throw new BadRequestException("La fecha de ingreso del paciente no puede ser nula");
        }
        if (paciente.getDomicilio() == null){
            throw new BadRequestException("El domicilio del paciente no puede ser nulo");
        }
        // si se valida y fue ok se procede a guardar el paciente
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        logger.info("El paciente " + pacienteGuardado.getId() + " fue guardado ");

        return pacienteGuardado;
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public List<Paciente> buscarTodos() {
        return pacienteRepository.findAll();
    }

    @Override
    public void modificarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    @Override
    public void eliminarPaciente(Integer id) {
        //tengo que chequear si el paciente existe o no existe, para poder lanzar la exception
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findById(id);
        if(pacienteEncontrado.isPresent()){
            pacienteRepository.deleteById(id);
        }else {
            throw  new ResourceNotFoundException("El paciente "+ id + " no fue encontrado");
        }

    }

    @Override
    public List<Paciente> buscarPorApellidoyNombre(String apellido, String nombre) {
        return pacienteRepository.findByApellidoAndNombre(apellido,nombre);
    }

    @Override
    public List<Paciente> buscarLikeNombre(String nombre) {
        return pacienteRepository.findByNombreLike(nombre);
    }
}
//**** aca hay un problema



