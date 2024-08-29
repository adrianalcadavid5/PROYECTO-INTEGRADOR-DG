package com.dh.clinica.controller;

import com.dh.clinica.entity.Odontologo;
import com.dh.clinica.entity.Paciente;
import com.dh.clinica.service.impl.OdontologoService;
import com.dh.clinica.service.impl.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//me permite trabajar con una tecnologia de vistas @Controller, genera una vista interna
@Controller
public class VistaController {
    //atributos
    private PacienteService pacienteService;
    private OdontologoService odontologoService;

    //constructor
    public VistaController(PacienteService pacienteService, OdontologoService odontologoService) {
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }

    /*Formas en las que me pueden pasar por parametro el idl:
    localhost:8080/1  de esta forma se utiliza la anotacion @PathVariable
    se utiliza cuando me dan el id ejemplo 20, clic sobre y producto y obtener la informacion
    localhost:8080?id=1 de esta forma utilizo la anotacipon @RequestParams.  en este caso http://localhost:8080/index?id=1
    esta se utiliza cuando estoy haciendo una busqueda de mas de un parámetro ejemplo ?id=1&nombre=carlos .....(campo Serrch)
     */
    //Esto es un endpoint
    @GetMapping("/paciente1")
    public String mostrarPacientePorId(Model model, @RequestParam Integer id){
        Paciente paciente = pacienteService.buscarPorId(id).get();
        model.addAttribute("nombre", paciente.getNombre());
        model.addAttribute("apellido", paciente.getApellido());
        //retornamos el nombre de la vista que esta en la carpeta templates -  paciente.html
        return "paciente";
    }
    //creamos este otro endpoint para  utilizar el otro metodo @PathVariable, tengo que decirle como se pasa el parametro
    @GetMapping("/paciente/{id}")
    public String mostrarPacientePorId2(Model model, @PathVariable Integer id){
        Paciente paciente = pacienteService.buscarPorId(id).get();
        model.addAttribute("nombre", paciente.getNombre());
        model.addAttribute("apellido", paciente.getApellido());
        //retornamos el nombre de la vista que esta en la carpeta templates -  paciente.html
        return "paciente";
    }
    @GetMapping("/odontologo/{id}")
    public String mostrarOdontologoPorId(Model model, @PathVariable Integer id){
        Odontologo odontologo = odontologoService.buscarPorId(id).get();
        model.addAttribute("nombre", odontologo.getNombre());
        model.addAttribute("apellido", odontologo.getApellido());
        return "odontologo";
    }

}
