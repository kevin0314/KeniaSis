package com.empresa.sircident.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.empresa.sircident.models.entities.Paciente;



public interface IPacienteDAO extends CrudRepository<Paciente, Long>{
	
	List<Paciente> findByNombreIgnoreCase(String nombre);
	
	@Query("FROM Paciente p WHERE p.nombre = TRIM(:#{#paciente.nombre}) "
			+"AND p.descripcion = TRIM(:#{#paciente.descripcion})"
			+"AND p.tratamiento = :#{#paciente.tratamiento} ")
	List<Paciente> validateIfExist(@Param("paciente") Paciente paciente);
}
