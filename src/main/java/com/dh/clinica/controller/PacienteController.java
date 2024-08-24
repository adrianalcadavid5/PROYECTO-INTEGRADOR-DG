package com.dh.clinica.controller;

import com.dh.clinica.model.Paciente;
import com.dh.clinica.service.PacienteService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Paciente> guardarPaciente(@RequestBody Paciente paciente){
        return ResponseEntity.ok(pacienteService.guardarPaciente(paciente));
    }
//el ResponseEntity me debe de devolver un paciente o un mensaje, cuando no estamos seguro que me va a devolver utilizo ?
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        Paciente paciente = pacienteService.buscarPorId(id);
        if(paciente != null){
            return ResponseEntity.ok(paciente);
        } else {
            // ResponseEntity.status(HttpStatus.NOT_FOUND).body("paciente no encontrado");
            //ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
    }

    @GetMapping("/buscartodos")
    public ResponseEntity<List<Paciente>> buscarTodos(){
        return ResponseEntity.ok(pacienteService.buscarTodos());
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarPaciente(@RequestBody Paciente paciente){
        Paciente pacienteEncontrado = pacienteService.buscarPorId(paciente.getId());
        if(pacienteEncontrado!= null){
            pacienteService.modificarPaciente(paciente);
            return ResponseEntity.ok("El paciente fue modificado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Integer id){
        Paciente pacienteEncontrado = pacienteService.buscarPorId(id);
        if(pacienteEncontrado!= null){
            pacienteService.eliminarPaciente(id);
            return ResponseEntity.ok("El paciente fue eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
