package com.dh.clinica.service.impl;


import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.exception.BadRequestException;
import com.dh.clinica.exception.ResourceNotFoundException;
import com.dh.clinica.repository.IOdontologoRepository;
import com.dh.clinica.service.IOdontologoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        validarOdontologo(odontologo);
        //si se valida y esta ok, se guarda el odontologo
        Odontologo odontologoGuardado = iOdontologoRepository.save(odontologo);
        logger.info("El odontologo " + odontologoGuardado.getId() + " fue guardado");
        return odontologoGuardado;
    }

    @Override
    public Optional<Odontologo> buscarPorId(Integer id) {
        Optional<Odontologo> odontologoEncontrado = iOdontologoRepository.findById(id);
        if (odontologoEncontrado.isPresent()){
            logger.info("El odontolgo con el ID "+ id + " fue encontrado");
            return odontologoEncontrado;
        }else {
            logger.warn("El odontologo con el ID "+ id + " no fue encontrado");
            throw new ResourceNotFoundException("El odontologo con el ID "+ id + " no fue encontrado");
        }
    }

    @Override
    public List<Odontologo> buscarTodos() {
        List<Odontologo> odontologos = iOdontologoRepository.findAll();
        if (odontologos.isEmpty()){
            logger.warn("No se encontraron odontologos.");
        }else {
            logger.info("Se encontraron "+ odontologos.size() + " odontologos");
        }
        return odontologos;
    }

    @Override
    public void modificarOdontologo(Odontologo odontologo) {
        validarOdontologo(odontologo);
        iOdontologoRepository.save(odontologo);
        logger.info("El odontologo " + odontologo.getId()+ " fue modificado");
    }

    @Override
    public void eliminarOdontologo(Integer id) {
        //tengo que chequear si el paciente existe o no existe, para poder lanzar la exception
        Optional<Odontologo> odontologoEncontrado = iOdontologoRepository.findById(id);
        if (odontologoEncontrado.isPresent()){
            iOdontologoRepository.deleteById(id);
            logger.info("El odontologo con el ID "+ id + " fue eliminado");
        } else {
            logger.error("El odontologo con el ID " + id + " no fue encontrado para eliminación");
            throw  new ResourceNotFoundException("El odontologo "+ id + " no fue encontrado");
        }
    }

    @Override
    public List<Odontologo> buscarPorApellidoyNombre(String apellildo, String nombre) {
        return iOdontologoRepository.findByApellidoAndNombre(apellildo,nombre);
    }

    @Override
    public List<Odontologo> buscarPorNombreOApelido(String nombre, String apellido) {
        logger.info("Iniciando búsqueda de odontólogos con nombre: {} y apellido: {}", nombre, apellido);

        List<Odontologo> odontologos;

        if (nombre != null && !nombre.isEmpty()) {
            logger.debug("Buscando odontólogos por nombre: {}", nombre);
            odontologos = iOdontologoRepository.findByNombre(nombre);
        } else if (apellido != null && !apellido.isEmpty()) {
            logger.debug("Buscando odontólogos por apellido: {}", apellido);
            odontologos = iOdontologoRepository.findByApellido(apellido);
        } else {
            logger.warn("No se proporcionó nombre ni apellido para la búsqueda");
            odontologos = Collections.emptyList(); // Retorna una lista vacía si no se proporciona nombre ni apellido
        }

        logger.info("Número de odontólogos encontrados: {}", odontologos.size());
        return odontologos;
    }

    @Override
    public List<Odontologo> buscarPorNombre(String nombre) {
        logger.debug("Buscando odontólogos por nombre: {}", nombre);
        List<Odontologo> odontologos = iOdontologoRepository.findByNombre(nombre);
        logger.info("Número de odontólogos encontrados por nombre {}: {}", nombre, odontologos.size());
        return odontologos;
    }

    @Override
    public List<Odontologo> buscarPorApellido(String apellido) {
        logger.debug("Buscando odontólogos por apellido: {}", apellido);
        List<Odontologo> odontologos = iOdontologoRepository.findByApellido(apellido);
        logger.info("Número de odontólogos encontrados por apellido {}: {}", apellido, odontologos.size());
        return odontologos;
    }

    private void validarOdontologo(Odontologo odontologo){
        if (odontologo.getNumeroMatricula() == null || odontologo.getNumeroMatricula().isEmpty()){
            throw new BadRequestException("El numero de la matricula del odontologo no puede estar vacio");
        }
        if (odontologo.getNombre() == null || odontologo.getNombre().isEmpty()){
            throw new BadRequestException("El nombre del odontologo no puede estar vacio");
        }
        if (odontologo.getApellido() == null || odontologo.getApellido().isEmpty()){
            throw new BadRequestException("El apellido del odontologo no puede estar vacio");
        }
    }
}
