package com.empresa.sircident.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.empresa.sircident.models.entities.Cliente;



public interface IClienteDAO extends CrudRepository<Cliente, Long>{
	
	List<Cliente> findByNombreIgnoreCase(String nombre);
	
}
