package com.empresa.sircident.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.sircident.models.entities.Expediente;
import com.empresa.sircident.services.interfaces.IExpedienteService;



@RestController
@RequestMapping("/api/expedientes")
@CrossOrigin(origins = "*")
public class ExpedienteController {

	@Autowired
	private IExpedienteService expedienteService;
	
	@GetMapping("/all")
	public List<Expediente> findAllExpedientes(){
		return expedienteService.findAll();
	}
	
	@PostMapping("/save")
	public ResponseEntity<Expediente> saveExpediente(@RequestBody Expediente expediente){
		if((expedienteService.findByNombre(expediente.getNombre()).size()<0) 
				&& expediente.getId() == null) {
			return ResponseEntity.status(409).build();
		}else {
			expedienteService.saveOrUpdate(expediente);
			return ResponseEntity.ok(expediente);
		}
	}
	
	@PostMapping("/delete")
	public ResponseEntity<Expediente> deleteExpediente(@RequestBody Expediente expediente){
		try {
			expedienteService.deleteExpediente(expediente);
		}catch(DataIntegrityViolationException e) {
			return ResponseEntity.status(510).build();
		}
		return ResponseEntity.ok().build();
	}
}
