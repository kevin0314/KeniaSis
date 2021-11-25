package com.empresa.sircident.controllers;

import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.sircident.models.entities.Paciente;
import com.empresa.sircident.services.interfaces.IPacienteService;



@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")

public class PacienteController {
private Logger logger = LoggerFactory.getLogger(PacienteController.class);
	
	@Autowired
	IPacienteService pacienteService;
	
	
	@Autowired
	ServletContext context;
	
	@GetMapping("/all")
	public List<Paciente> findAllPacientes(){
		return pacienteService.findAll();
	}
	
	@PostMapping("/save")
	public ResponseEntity<Paciente> savePaciente(@RequestBody Paciente paciente){
		if((pacienteService.findByNombre(paciente.getNombre()).size()<0) 
				&& paciente.getId() == null) {
			return ResponseEntity.status(409).build();
		}else {
			pacienteService.saveOrUpdate(paciente);
			return ResponseEntity.ok(paciente);
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<Paciente> deletePaciente(@RequestBody Paciente paciente){
		try {
			pacienteService.deletePaciente(paciente);
		}catch(DataIntegrityViolationException e) {
			return ResponseEntity.status(510).build();
		}
		return ResponseEntity.ok().build();
	}
	
}
