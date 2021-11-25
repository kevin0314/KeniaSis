package com.empresa.sircident.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.sircident.models.dao.ITratamientoDAO;
import com.empresa.sircident.models.entities.Tratamiento;
import com.empresa.sircident.services.interfaces.ITratamientoService;

@Service
public class TratamientoServiceImpl implements ITratamientoService {
	
	@Autowired
	private ITratamientoDAO tratamientoDAO;
	
	@Override
	public List<Tratamiento> findAll() {
		return (List<Tratamiento>)tratamientoDAO.findAll();
	}

	@Override
	public void saveOrUpdate(Tratamiento tratamiento) {
		tratamientoDAO.save(tratamiento);
	}

	@Override
	public List<Tratamiento> findByNombre(String nombre) {
		return tratamientoDAO.findByNombreIgnoreCase(nombre);
	}

	@Override
	public void deleteTratamiento(Tratamiento tratamiento) {
		tratamientoDAO.delete(tratamiento);

	}


}
