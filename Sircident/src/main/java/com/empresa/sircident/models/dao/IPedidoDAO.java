package com.empresa.sircident.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.empresa.sircident.models.entities.Pedido;



public interface IPedidoDAO extends CrudRepository<Pedido,Long>{
	
	@Query("FROM Pedido p WHERE p.estado='R' ORDER BY p.id DESC")
	List<Pedido> findAllRecibidos();
	
	@Query("FROM Pedido p WHERE p.estado='D' ORDER BY p.id DESC")
	List<Pedido> findAllDespachados();
	
	@Query("FROM Pedido p WHERE p.estado='A' ORDER BY p.id DESC")
	List<Pedido> findAllAnulados();
	
	@Query("FROM Pedido p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFinal ORDER BY p.fechaPedido DESC")
	List<Pedido> findAllWithRangeDates(@Param("fechaInicio") Date fechaInicio, @Param("fechaFinal") Date fechaFinal);

}