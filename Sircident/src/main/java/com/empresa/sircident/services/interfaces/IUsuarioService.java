package com.empresa.sircident.services.interfaces;

import java.util.List;

import com.empresa.sircident.models.entities.Usuario;

public interface IUsuarioService {
	
	List<Usuario> findAll();
	
	List<Usuario> findAllInactivos();
	
	void saveOrUpdate(Usuario usuario);
	
	List<Usuario> findByNombre(String nombre);
	
	Usuario findOneNombre(String nombre);
	
	Usuario changeState(Usuario usuario);
}
