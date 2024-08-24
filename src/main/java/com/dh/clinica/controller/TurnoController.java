package com.dh.clinica.controller;

import com.dh.clinica.model.Turno;
import com.dh.clinica.service.TurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarTurno(@RequestBody Turno turno){
        Turno turnoAGuardar = turnoService.guardarTurno(turno);
        if(turnoAGuardar != null){
            return ResponseEntity.ok(turnoAGuardar);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El paciente o el odontologo no fueron encontrados");
        }

    }
    @GetMapping("/buscartodos")
    public ResponseEntity<List<Turno>> buscarTodos(){
        return ResponseEntity.ok(turnoService.buscarTodos());
    }
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        Turno turno = turnoService.buscarPorId(id);
        if (turno != null){
           return ResponseEntity.ok(turno);
        }else{
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El turno no fue encontrado");
        }
    }
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarTurno(@RequestBody Turno turno){
        Turno turnoEncontrado = turnoService.buscarPorId(turno.getId());
        if (turnoEncontrado != null){
            turnoService.modificarTurno(turno);
            return ResponseEntity.ok("El turno fue modificado");
        }else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El turno a modificar no fue encontrado");
        }
    }
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarTurno(@PathVariable Integer id){
        Turno turnoEncontrado = turnoService.buscarPorId(id);
        if (turnoEncontrado != null){
            turnoService.eliminarTurno(id);
            return ResponseEntity.ok("El turno fue eliminado");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El turno a eliminar no fue encontrado");
        }
    }

}
