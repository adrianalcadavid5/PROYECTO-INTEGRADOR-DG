package com.dh.clinica.controller;

import com.dh.clinica.model.Paciente;
import com.dh.clinica.service.PacienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
    private PacienteService pacienteService;

    //para hacer una inyeccion de dependencia, lo hacemos por medio del constructor, podemos acceder a los metodos del servicio
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }


    /*aca ocurre una serializacion y deserializacion de datos
    ingresa -> JSON -> jackson -> objeto Paciente (deserealizacion)
    salga -> Objeto Paciente -> jackson -> JSON (serializacion) (esto ya lo hace spring boot
    NOTA: Las clases del moldelo deben de tener un consturctor vacio*/
    //creamos un endpoint
    @PostMapping("/guardar")
        public Paciente guardarPaciente(@RequestBody Paciente paciente){
        return pacienteService.guardarPaciente(paciente);
    }
    @GetMapping("/buscar/{id}")
    public Paciente buscarPorId(@PathVariable Integer id){
        return pacienteService.buscarPorId(id);
    }
    @GetMapping("/buscartodos")
    public List<Paciente> buscarTodos(){
        return pacienteService.buscarTodos();
    }
    @PutMapping("/modificar")
    public String modificarPaciente(@RequestBody Paciente paciente){
        pacienteService.modificarPaciente(paciente);
        return "el paciente fue modificado";
    }
    @DeleteMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Integer id){
        pacienteService.eliminarPaciente(id);
        return "el paciente fue eliminado";
    }
}
