package com.empresa.sircident.models.entities;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuarios", schema="public", catalog="SircidentDB")
public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	@Column(name="nombre", nullable=false, length=100)
	private String nombre;
	
	@Basic
	@Column(name="login", nullable=false, length=50)
	private String login;
	
	@Basic
	@Column(name="passwd", nullable=false, length=300)
	private String passwd;
	
	@Basic
	@Column(name="estado", nullable=false, length=1)
	private String estado;

	
	@PrePersist
	private void setEstado() {
	this.estado = "A";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
}