package com.empresa.sircident.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.sircident.models.dao.IPacienteDAO;
import com.empresa.sircident.models.entities.Paciente;
import com.empresa.sircident.services.interfaces.IPacienteService;

@Service
public class PacienteServiceImpl implements IPacienteService{

	
	//inyectando ICategoriaDAO
	@Autowired
	private IPacienteDAO pacienteDAO;
	
	@Override
	public List<Paciente> findAll() {
		return (List<Paciente>)pacienteDAO.findAll();
	}

	@Override
	public void saveOrUpdate(Paciente Paciente) {
		pacienteDAO.save(Paciente);
	}

	@Override
	public List<Paciente> findByNombre(String nombre) {
		return pacienteDAO.findByNombreIgnoreCase(nombre);
	}
	@Override
	public List<Paciente> validateIfExist(Paciente paciente) {
		return pacienteDAO.validateIfExist(paciente);
	}
	@Override
	public void deletePaciente(Paciente paciente) {
		pacienteDAO.delete(paciente);

	}

}

