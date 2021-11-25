package com.empresa.sircident.services.interfaces;

import java.util.Date;
import java.util.List;

import com.empresa.sircident.models.entities.Pedido;

public interface IPedidoService {
	
	List<Pedido> findAll(Date fecha);
	
	List<Pedido> findAllRecibidos();
	
	List<Pedido> findAllDespachados();
	
	List<Pedido> findAllAnulados();
	
	Pedido saveOrUpdate(Pedido pedido);
	
	Pedido changeState(Pedido pedido);
	
	List<Pedido> findAllDespachados(Date fechaFin);

}
