package com.empresa.sircident.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.sircident.models.entities.Pedido;
import com.empresa.sircident.services.interfaces.IPedidoService;



@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
	
	@Autowired
	private IPedidoService pedidoService;
	
	@GetMapping("/all")
	public List<Pedido> findAll(@RequestParam(name = "fecha",
			required=false) Date fecha){
		
		return pedidoService.findAll(fecha);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Pedido> saveOrUpdate(@RequestBody Pedido pedido){
		try {
			return ResponseEntity.ok(pedidoService.saveOrUpdate(pedido));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(500).build();
	}
	
	@PostMapping("/change-state")
	public ResponseEntity<Pedido> changeState(@RequestBody Pedido pedido,
			@RequestParam(name="estado") String estado){
		pedido.setEstado(estado);
		return ResponseEntity.ok(pedidoService.changeState(pedido));
	}

}
