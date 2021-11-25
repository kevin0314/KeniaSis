package com.empresa.sircident.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.empresa.sircident.models.entities.Expediente;


public interface IExpedienteDAO extends CrudRepository<Expediente, Long>{
	
	List<Expediente> findByNombreIgnoreCase(String nombre);

}