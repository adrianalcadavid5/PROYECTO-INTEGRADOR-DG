package com.dh.clinica.controller;

import com.dh.clinica.model.Odontologo;
import com.dh.clinica.service.OdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologo")
public class OdontologoController {

    private OdontologoService odontologoService;
    public  OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

        @PostMapping("/guardar")
        public ResponseEntity<Odontologo> guardarOdontologo(@RequestBody Odontologo odontologo){
            return ResponseEntity.ok(odontologoService.guardarOdontologo(odontologo));
        }

        @GetMapping("/buscar/{id}")
        public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
            Odontologo odontologo = odontologoService.buscarPorId(id);
            if (odontologo != null){
                return ResponseEntity.ok(odontologo);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El odontologo no fue encontrado");
            }

        }

        @GetMapping("/buscartodos")
        public ResponseEntity<List<Odontologo>> buscarTodos(){
            return ResponseEntity.ok(odontologoService.buscarTodos());
        }

        @PutMapping("/modificar")
        public ResponseEntity<?> modificarPaciente(@RequestBody Odontologo odontologo){
            Odontologo odontologoEncontrado = odontologoService.buscarPorId(odontologo.getId());
            if (odontologo !=null){
                odontologoService.modificarOdontologo(odontologo);
                return ResponseEntity.ok("El odontologo fue modificado");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El odontologo no fue modificado");
            }
        }

        @DeleteMapping("/eliminar/{id}")
        public ResponseEntity<?> eliminarOdontologo(@PathVariable Integer id){
            Odontologo odontologoEncontrado = odontologoService.buscarPorId(id);
            if (odontologoEncontrado != null){
                odontologoService.eliminarOdontologo(id);
                return ResponseEntity.ok("El odontologo fue eliminado");
            }else{
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El odontologo a eliminar no fue encontrado");
            }
        }
    }

