package com.empresa.sircident.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.sircident.models.dao.IExpedienteDAO;
import com.empresa.sircident.models.entities.Expediente;
import com.empresa.sircident.services.interfaces.IExpedienteService;

@Service
public class ExpedienteServiceImpl implements IExpedienteService {

	@Autowired
	private IExpedienteDAO expedienteDAO;
	
	@Override
	public List<Expediente> findAll() {
		return (List<Expediente>)expedienteDAO.findAll();
	}

	@Override
	public void saveOrUpdate(Expediente expediente) {
		expedienteDAO.save(expediente);
	}

	@Override
	public List<Expediente> findByNombre(String nombre) {
		return expedienteDAO.findByNombreIgnoreCase(nombre);
	}

	@Override
	public void deleteExpediente(Expediente expediente) {
		expedienteDAO.delete(expediente);

	}

}
