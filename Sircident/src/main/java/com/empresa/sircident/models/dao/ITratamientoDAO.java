package com.empresa.sircident.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.empresa.sircident.models.entities.Tratamiento;

public interface ITratamientoDAO extends CrudRepository<Tratamiento, Long>{
	List<Tratamiento> findByNombreIgnoreCase(String nombre);
}