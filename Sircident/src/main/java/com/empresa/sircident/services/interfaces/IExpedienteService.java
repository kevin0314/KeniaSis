package com.empresa.sircident.services.interfaces;

import java.util.List;

import com.empresa.sircident.models.entities.Expediente;

public interface IExpedienteService {
	
	List<Expediente> findAll();

	void saveOrUpdate(Expediente expediente);

	List<Expediente> findByNombre(String nombre);

	void deleteExpediente(Expediente expediente);

}
