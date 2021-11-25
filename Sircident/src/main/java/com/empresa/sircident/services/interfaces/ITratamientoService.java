package com.empresa.sircident.services.interfaces;

import java.util.List;

import com.empresa.sircident.models.entities.Tratamiento;

public interface ITratamientoService {
	
	List<Tratamiento> findAll();

	void saveOrUpdate(Tratamiento tratamiento);

	List<Tratamiento> findByNombre(String nombre);

	void deleteTratamiento(Tratamiento tratamiento);
}
