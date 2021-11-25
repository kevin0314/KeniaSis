package com.empresa.sircident.models.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pedidos", schema="public", catalog="SircidentDB")
public class Pedido implements Serializable{
	private static final long serialVersionUID = 1L;
	  
	  @Id
	  @Column(name = "id", nullable = true)
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  @Basic
	  @Column(name = "fecha_pedido", nullable = false)
	  @JsonFormat(timezone = "America/El_Salvador", pattern = "dd-MM-yyyy")
	  @Temporal(TemporalType.DATE)
	  @DateTimeFormat(pattern = "dd-MM-yyyy")
	  private Date fechaPedido;
	  @Basic
	  @Column(name = "fecha_despacho", nullable = true)
	  @JsonFormat(timezone = "America/El_Salvador", pattern = "dd-MM-yyyy")
	  @Temporal(TemporalType.DATE)
	  @DateTimeFormat(pattern = "dd-MM-yyyy")
	  private Date fechaDespacho;
	  
	  @Basic
	  @Column(name = "total", nullable = false, precision = 2)
	  private Double total;
	  @Basic
	  @Column(name = "estado", nullable = false, length = 1)
	  private String estado;

	  @ManyToOne(fetch = FetchType.LAZY)
	  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	  @JoinColumn(name = "idcliente", referencedColumnName = "id", nullable = false)
	  private Cliente cliente;
	  
	  // coleccion de tipo DetallePedido
	  @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<DetallePedido> detallePedido;
	  
	  public Long getId() {
	    return id;
	  }

	  public void setId(Long id) {
	    this.id = id;
	  }

	  public Date getFechaPedido() {
	    return fechaPedido;
	  }

	  public void setFechaPedido(Date fechaPedido) {
	    this.fechaPedido = fechaPedido;
	  }

	  public Date getFechaDespacho() {
	    return fechaDespacho;
	  }

	  public void setFechaDespacho(Date fechaDespacho) {
	    this.fechaDespacho = fechaDespacho;
	  }

	  public Double getTotal() {
	    return total;
	  }

	  public void setTotal(Double total) {
	    this.total = total;
	  }

	  public String getEstado() {
	    return estado;
	  }

	  public void setEstado(String estado) {
	    this.estado = estado;
	  }

	  public Cliente getCliente() {
	    return cliente;
	  }

	  public void setCliente(Cliente cliente) {
	    this.cliente = cliente;
	  }

	  public List<DetallePedido> getDetallePedido() {
	    return detallePedido;
	  }

	  public void setDetallePedido(List<DetallePedido> detallePedido) {
	    this.detallePedido = detallePedido;
	  }
	  
	  
	  @PrePersist
	  private void setEstado() {
	    this.estado = "R";
	  }


	}