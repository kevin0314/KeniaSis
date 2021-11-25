package com.empresa.sircident.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.sircident.models.dao.IUsuarioDAO;
import com.empresa.sircident.models.entities.Usuario;
import com.empresa.sircident.services.interfaces.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Override
	public List<Usuario> findAll() {
		return usuarioDAO.finAllActivos();
	}

	@Override
	public List<Usuario> findAllInactivos() {
		return usuarioDAO.finAllInactivos();
	}

	@Override
	public void saveOrUpdate(Usuario usuario) {
		usuarioDAO.save(usuario);
	}

	@Override
	public List<Usuario> findByNombre(String nombre) {
		return usuarioDAO.findByNombreIgnoreCase(nombre);
	}

	@Override
	public Usuario findOneNombre(String nombre) {
		return usuarioDAO.findByNombre(nombre);
	}

	@Override
	public Usuario changeState(Usuario usuario) {
		this.saveOrUpdate(usuario);
		return usuario;
	}

}
