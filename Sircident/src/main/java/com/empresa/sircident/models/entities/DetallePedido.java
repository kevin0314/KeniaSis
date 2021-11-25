package com.empresa.sircident.models.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="detalle_pedidos", schema="public", catalog="SircidentDB")
public class DetallePedido implements Serializable{
	private static final long serialVersionUID = 1L;
	  
	  @Id
	  @Column(name = "id", nullable = false)
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  @Basic
	  @Column(name = "cantidad", nullable = false)
	  private int cantidad;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	  @JoinColumn(name = "idpaciente", referencedColumnName = "id", nullable = false)
	  private Paciente paciente;

	  @ManyToOne(fetch = FetchType.LAZY)
	  @JsonBackReference
	  @JoinColumn(name = "idpedido", referencedColumnName = "id", nullable = false)
	  private Pedido pedido;

	  public Long getId() {
	    return id;
	  }

	  public void setId(Long id) {
	    this.id = id;
	  }

	  public int getCantidad() {
	    return cantidad;
	  }

	  public void setCantidad(int cantidad) {
	    this.cantidad = cantidad;
	  }

	  public Paciente getPaciente() {
	    return paciente;
	  }

	  public void setCombo(Paciente paciente) {
	    this.paciente = paciente;
	  }

	  public Pedido getPedido() {
	    return pedido;
	  }

	  public void setPedido(Pedido pedido) {
	    this.pedido = pedido;
	  }
	    

	}