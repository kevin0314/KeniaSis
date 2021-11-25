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

import com.empresa.sircident.models.entities.Cliente;
import com.empresa.sircident.services.interfaces.IClienteService;




@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")

public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/all")
	public List<Cliente> findAllClientes(){
		return clienteService.findAll();
	}
	
	@PostMapping("/save")
	public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente){
		if((clienteService.findByNombre(cliente.getNombre()).size()>0) && cliente.getId() == null){
			return ResponseEntity.status(409).build();
			//409, CONFLICTO
		}else {
			clienteService.saveOrUpdate(cliente);
			return ResponseEntity.ok(cliente);
		}
	}
	
	@PostMapping("/delete")
	public ResponseEntity<Cliente> deleteCliente(@RequestBody Cliente cliente){
		try {
			
		}catch(DataIntegrityViolationException e){
			return ResponseEntity.status(510).build();
		}
		return ResponseEntity.ok().build();
	}
	
}
