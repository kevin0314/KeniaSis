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

import com.empresa.sircident.models.entities.Tratamiento;
import com.empresa.sircident.services.interfaces.ITratamientoService;



@RestController
@RequestMapping("/api/tratamientos")
@CrossOrigin(origins = "*")

public class TratamientoController {
	@Autowired
	private ITratamientoService tratamientoService;
	
	@GetMapping("/all")
	public List<Tratamiento> findAllTratamientos(){
		return tratamientoService.findAll();
	}
	
	@PostMapping("/save")
	public ResponseEntity<Tratamiento> saveTratamiento(@RequestBody Tratamiento tratamiento){
		if((tratamientoService.findByNombre(tratamiento.getNombre()).size()<0) 
				&& tratamiento.getId() == null) {
			return ResponseEntity.status(409).build();
		}else {
			tratamientoService.saveOrUpdate(tratamiento);
			return ResponseEntity.ok(tratamiento);
		}
	}
	
	@PostMapping("/delete")
	public ResponseEntity<Tratamiento> deleteTratamiento(@RequestBody Tratamiento tratamiento){
		try {
			tratamientoService.deleteTratamiento(tratamiento);
		}catch(DataIntegrityViolationException e) {
			return ResponseEntity.status(510).build();
		}
		return ResponseEntity.ok().build();
	}


}
