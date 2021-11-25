package com.empresa.sircident.services.interfaces;

import java.util.List;

import com.empresa.sircident.models.entities.Paciente;

public interface IPacienteService {
	List<Paciente> findAll();

	void saveOrUpdate(Paciente paciente);

	List<Paciente> findByNombre(String nombre);

	void deletePaciente(Paciente paciente);

	List<Paciente> validateIfExist(Paciente paciente);

}
