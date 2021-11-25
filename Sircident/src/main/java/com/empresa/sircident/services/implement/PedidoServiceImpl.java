package com.empresa.sircident.services.implement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.sircident.models.dao.IDetallePedidoDAO;
import com.empresa.sircident.models.dao.IPedidoDAO;
import com.empresa.sircident.models.entities.DetallePedido;
import com.empresa.sircident.models.entities.Pedido;
import com.empresa.sircident.services.interfaces.IPedidoService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PedidoServiceImpl implements IPedidoService {
	
	// Inyectando IPedidoDAO
		  @Autowired
		  private IPedidoDAO pedidoDAO;
		  
		  // Inyectando IDetallePedidoDAO
		  @Autowired
		  private IDetallePedidoDAO detallePedidoDAO;
		  
		  @Transactional(readOnly = true)
		  
		  @Override
		  public List<Pedido> findAll(Date fechaInicio) {
		    Date fechaFin = null;
		    if(fechaInicio != null) {
		      Calendar c = Calendar.getInstance();
		      c.setTime(fechaInicio);
		      c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
		      fechaFin = c.getTime();
		      return pedidoDAO.findAllWithRangeDates(fechaInicio, fechaFin);
		    }
		    return pedidoDAO.findAllRecibidos();
		    
		  }
		  
		  
		  @Override
		  public List<Pedido> findAllDespachados(Date fechaFin) {
		    Date fechaInicio = null;
		    if(fechaFin != null) {
		      Calendar c = Calendar.getInstance();
		      c.setTime(fechaFin);
		      c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
		      fechaFin = c.getTime();
		      return pedidoDAO.findAllWithRangeDates(fechaInicio, fechaFin);
		    }
		    return pedidoDAO.findAllDespachados();
		  }

		  @Override
		  public List<Pedido> findAllRecibidos() {
		    return pedidoDAO.findAllRecibidos();
		  }

		  @Override
		  public List<Pedido> findAllDespachados() {
		    return pedidoDAO.findAllDespachados();
		  }

		  @Override
		  public List<Pedido> findAllAnulados() {
		    return pedidoDAO.findAllAnulados();
		  }
		  
		  
		  @Transactional
		  @Override
		  public Pedido saveOrUpdate(Pedido pedido) {
		    Pedido pedidoPersisted = null;
		    try {
		      if(pedido.getId() == null) {
		        List<DetallePedido> detallePedidos = pedido.getDetallePedido();
		        pedido.setDetallePedido(new ArrayList<DetallePedido>());
		        pedidoPersisted = pedidoDAO.save(pedido);
		        
		        for(DetallePedido detalle : detallePedidos) {
		          detalle.setPedido(pedidoPersisted);
		        }
		        detallePedidoDAO.saveAll(detallePedidos);
		      }else {
		        for(DetallePedido detalle : pedido.getDetallePedido()) {
		          detalle.setPedido(pedido);
		        }
		        pedidoDAO.save(pedido);
		      }
		      
		    }catch(Exception e) {
		      //log.info("Error: "+e.getMessage());
		      System.out.print("Error: " +e.getMessage());
		    }
		    return null;
		  }

		  @Override
		  public Pedido changeState(Pedido pedido) {
		    this.saveOrUpdate(pedido);
		    return pedido;
		   
		  }

}