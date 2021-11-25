package com.empresa.sircident.services.interfaces;

import java.util.List;

import com.empresa.sircident.models.entities.Cliente;

import com.empresa.sircident.models.entities.Cliente;

public interface IClienteService {
	
List<Cliente> findAll();
	
	void saveOrUpdate(Cliente cliente);
	
	List<Cliente> findByNombre(String nombre);
	
	void deleteCliente(Cliente cliente);
}
