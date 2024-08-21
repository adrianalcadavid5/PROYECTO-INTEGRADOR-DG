package com.dh.clinica.service;
import com.dh.clinica.dao.IDao;
import com.dh.clinica.model.Paciente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {
    //tienen una implementacion de pacienteIDao y esto obliga a un constructor
    private IDao<Paciente> pacienteIDao;

    public PacienteService(IDao<Paciente> pacienteIDao) {
        this.pacienteIDao = pacienteIDao;
    }

    //aca implementamos los metodos que trabajos en dao Impl ejemplo PacienteDaoH2
    public Paciente guardarPaciente(Paciente paciente){
        return pacienteIDao.guardar(paciente);
    }
    public Paciente buscarPorId(Integer id){
        return pacienteIDao.buscarPorId(id); // despues vamos al test
    }
    public List<Paciente>buscarTodos(){
        return pacienteIDao.buscarTodos();
    }
    public void modificarPaciente(Paciente paciente){
        pacienteIDao.modificar(paciente);
    }
    public void eliminarPaciente(Integer id){
        pacienteIDao.eliminar(id);
    }

}
