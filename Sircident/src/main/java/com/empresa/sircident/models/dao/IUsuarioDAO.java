package com.empresa.sircident.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.empresa.sircident.models.entities.Usuario;



public interface IUsuarioDAO extends CrudRepository<Usuario, Long> {

	@Query("FROM Usuario c WHERE c.estado='A' ORDER BY c.id DESC")
	List<Usuario> finAllActivos();

	@Query("FROM Usuario c WHERE c.estado='I' ORDER BY c.id DESC")
	List<Usuario> finAllInactivos();

	List<Usuario> findByNombreIgnoreCase(String nombre);

	Usuario findByNombre(String nombre);
	
}