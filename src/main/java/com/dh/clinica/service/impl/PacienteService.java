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
        //valido sus datos usando ValidarPaciente realizado al final
        validarPaciente(paciente);
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        logger.info("El paciente "+ pacienteGuardado.getId() + " fue guardado.");
        return pacienteGuardado;
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findById(id);
        if (pacienteEncontrado.isPresent()){
            logger.info("El paciente con el ID " + id + " fue encontrado");
            return pacienteEncontrado;
        }else {
            logger.warn("El paciente con el ID "+ id + " no fue encontrado");
            throw new ResourceNotFoundException("El paciente coon el ID "+ id + " no fue encontrado");
        }
    }

    @Override
    public List<Paciente> buscarTodos() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        if (pacientes.isEmpty()){
            logger.warn("No se encontraron pacientes.");
        }else {
            logger.info("Se encontraron "+ pacientes.size() + " pacientes.");
        }
        return pacientes;
    }

    @Override
    public void modificarPaciente(Paciente paciente) {
        validarPaciente(paciente);
        pacienteRepository.save(paciente);
        logger.info("El paciente " + paciente.getId() + " fue modificado.");
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

    private void validarPaciente(Paciente paciente){
        if (paciente.getApellido() == null || paciente.getApellido().isEmpty()) {
            throw new BadRequestException("El apellido del paciente no puede estar vacío");
        }
        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()) {
            throw new BadRequestException("El nombre del paciente no puede estar vacío");
        }
        if (paciente.getDni() == null || paciente.getDni().isEmpty()) {
            throw new BadRequestException("El DNI del paciente no puede estar vacío");
        }
        if (paciente.getFechaIngreso() == null) {
            throw new BadRequestException("La fecha de ingreso del paciente no puede ser nula");
        }
        if (paciente.getDomicilio() == null) {
            throw new BadRequestException("El domicilio del paciente no puede ser nulo");
        }
    }
}
//**** aca hay un problema



