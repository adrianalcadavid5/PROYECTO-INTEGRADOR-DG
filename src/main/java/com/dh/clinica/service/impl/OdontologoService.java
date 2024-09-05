package com.dh.clinica.service.impl;


import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.exception.BadRequestException;
import com.dh.clinica.exception.ResourceNotFoundException;
import com.dh.clinica.repository.IOdontologoRepository;
import com.dh.clinica.service.IOdontologoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService implements IOdontologoService {
    private final Logger logger = LoggerFactory.getLogger(OdontologoService.class);
    private IOdontologoRepository iOdontologoRepository;

    public OdontologoService(IOdontologoRepository iOdontologoRepository) {
        this.iOdontologoRepository = iOdontologoRepository;
    }

    @Override
    public Odontologo guardarOdontologo(Odontologo odontologo) {
        //valido que el numeroMatricula, nombre y apellido no sean nullos o esten vacios
        if (odontologo.getNumeroMatricula() == null || odontologo.getNumeroMatricula().isEmpty()){
            throw new BadRequestException("El numero de la matricula del odontologo no puede estar vacio");
        }
        if (odontologo.getNombre() == null || odontologo.getNombre().isEmpty()){
            throw new BadRequestException("El nombre del odontologo no puede estar vacio");
        }
        if (odontologo.getApellido() == null || odontologo.getApellido().isEmpty()){
            throw new BadRequestException("El apellido del odontologo no puede estar vacio");
        }
        //si se valida y esta ok, se guarda el odontologo
        Odontologo odontologoGuardado = iOdontologoRepository.save(odontologo);
        logger.info("El odontologo " + odontologoGuardado.getId() + " fue guardado");
        return odontologoGuardado;
    }

    @Override
    public Optional<Odontologo> buscarPorId(Integer id) {
        return iOdontologoRepository.findById(id);
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return iOdontologoRepository.findAll();
    }

    @Override
    public void modificarOdontologo(Odontologo odontologo) {
        iOdontologoRepository.save(odontologo);
    }

    @Override
    public void eliminarOdontologo(Integer id) {
        Optional<Odontologo> odontologoEncontrado = iOdontologoRepository.findById(id);
        if (odontologoEncontrado.isPresent()){
            iOdontologoRepository.deleteById(id);
        } else {
            throw  new ResourceNotFoundException("El odontologo "+ id + " no fue encontrado");
        }

    }

    @Override
    public List<Odontologo> buscarPorApellidoyNombre(String apellildo, String nombre) {
        return iOdontologoRepository.findByApellidoAndNombre(apellildo,nombre);
    }

    @Override
    public List<Odontologo> buscarPorNombreOApelido(String nombre, String apellido) {
        return iOdontologoRepository.buscarPorNombreOApellido(nombre,apellido);
    }

    @Override
    public List<Odontologo> buscarPorNombre(String nombre) {
        return iOdontologoRepository.findByNombre(nombre);
    }

    @Override
    public List<Odontologo> buscarPorApellido(String apellido) {
        return iOdontologoRepository.findByApellido(apellido);
    }


}
