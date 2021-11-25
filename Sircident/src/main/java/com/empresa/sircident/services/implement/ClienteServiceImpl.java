package com.empresa.sircident.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.sircident.models.dao.IClienteDAO;
import com.empresa.sircident.models.entities.Cliente;
import com.empresa.sircident.services.interfaces.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService{
	
	@Autowired
	private IClienteDAO clienteDAO;

	@Override
	public List<Cliente> findAll() {
		return (List<Cliente>)clienteDAO.findAll();	
	}

	@Override
	public void saveOrUpdate(Cliente cliente) {
		clienteDAO.save(cliente);
	}

	@Override
	public List<Cliente> findByNombre(String nombre) {
		return clienteDAO.findByNombreIgnoreCase(nombre);
	}

	@Override
	public void deleteCliente(Cliente cliente) {
		clienteDAO.delete(cliente);
		
	}

	
}