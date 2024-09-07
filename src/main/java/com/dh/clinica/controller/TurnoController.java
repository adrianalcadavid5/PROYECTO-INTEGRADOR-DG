package com.dh.clinica.controller;

import com.dh.clinica.dto.request.TurnoModificarDto;
import com.dh.clinica.dto.request.TurnoRequestDto;
import com.dh.clinica.dto.response.TurnoResponseDto;
import com.dh.clinica.entity.Paciente;
import com.dh.clinica.entity.Turno;
import com.dh.clinica.exception.BadRequestException;
import com.dh.clinica.service.impl.TurnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarTurno(@Valid  @RequestBody TurnoRequestDto turnoRequestDto) {
        TurnoResponseDto turnoAGuardar = turnoService.guardarTurno(turnoRequestDto);
        return ResponseEntity.ok(turnoAGuardar);
    }

    @GetMapping("/buscartodos")
    public ResponseEntity<List<TurnoResponseDto>> buscarTodos(){
        return ResponseEntity.ok(turnoService.buscarTodos());
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarTurno(@Valid @RequestBody TurnoModificarDto turnoModificarDto){
        turnoService.modificarTurno(turnoModificarDto);
        return ResponseEntity.ok("{\"mensaje\": \"El turno fue modificado\"}");
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        Optional<TurnoResponseDto> turno = turnoService.buscarPorId(id);
        if (turno.isPresent()){
           return ResponseEntity.ok(turno.get());
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarTurno(@PathVariable Integer id){
            turnoService.eliminarTurno(id);
            return ResponseEntity.ok("{\"mensaje\": \"El turno fue eliminado\"}");
    }
    @GetMapping("buscarporapellidopaciente/{apellido}")
    public ResponseEntity<List<Turno>> buscarTurnoApellidoPaciente(@PathVariable String apellido){
        return ResponseEntity.ok(turnoService.buscarTurnoPaciente(apellido));
    }
    @GetMapping("/bucarporfecha/{fecha}")
    public ResponseEntity<List<Turno>> buscarTurnoFecha(@PathVariable("fecha") String fechaStr) {
        try {
            List<Turno> turnos = turnoService.buscarTurnosPorFecha(fechaStr);
            return ResponseEntity.ok(turnos);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);  // Retornar un error si la fecha no tiene el formato correcto
        }
    }

    @GetMapping("ordenartodos")
    public ResponseEntity<List<Turno>> ordenarTurnosPorFecha(){
        return ResponseEntity.ok(turnoService.ordenarTurnosPorFecha());
    }
}
