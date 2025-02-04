package com.samael.springboot.backend.jokes.models.dto;

import java.io.Serializable;
import java.util.List;


public class PrimeraVezDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
    private String programa;
    private String fechaEmision;
    private int joke_id;
    private List<String> telefonos;
	
    public PrimeraVezDTO() {
		super();
	}

	public PrimeraVezDTO(Integer id, String programa, String fechaEmision, int joke_id, List<String> telefonos) {
		super();
		this.id = id;
		this.programa = programa;
		this.fechaEmision = fechaEmision;
		this.joke_id = joke_id;
		this.telefonos = telefonos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public int getJoke_id() {
		return joke_id;
	}

	public void setJoke_id(int joke_id) {
		this.joke_id = joke_id;
	}

	public List<String> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<String> telefonos) {
		this.telefonos = telefonos;
	}
}
