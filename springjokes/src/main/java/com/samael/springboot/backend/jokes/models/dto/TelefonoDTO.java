package com.samael.springboot.backend.jokes.models.dto;

public class TelefonoDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String numero;
	private int primeraVez_id;

	public TelefonoDTO() {
		super();
	}

	public TelefonoDTO(int id, String numero, int primeraVez_id) {
		super();
		this.id = id;
		this.numero = numero;
		this.primeraVez_id = primeraVez_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public int getPrimeraVez_id() {
		return primeraVez_id;
	}

	public void setPrimeraVez_id(int primeraVez_id) {
		this.primeraVez_id = primeraVez_id;
	}
}
