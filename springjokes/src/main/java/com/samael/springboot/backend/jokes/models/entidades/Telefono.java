package com.samael.springboot.backend.jokes.models.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "telefonos")
public class Telefono implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

    @Size(min = 9, max = 9, message = "El número de teléfono debe tener exactamente 9 dígitos.")
    @Pattern(regexp = "^[6789]\\d{8}$", message = "El número debe empezar por 6, 7, 8 o 9 y tener 9 dígitos.")
	@Column(name = "numero", nullable = false, length = 9)
	private String numero;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_primeravez", nullable = false, foreignKey = @ForeignKey(name = "fk_primeravez"))
	@JsonIgnoreProperties(value = {"telefonos", "joke"}, allowSetters = true)
	private PrimeraVez primeraVez;

	// Constructor vacío
	public Telefono() {
	}

	// Constructor con parámetros
	public Telefono(int id, String numero, PrimeraVez primeraVez) {
		this.id = id;
		this.numero = numero;
		this.primeraVez = primeraVez;
	}

	// Getters y Setters
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

	public PrimeraVez getPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(PrimeraVez primeraVez) {
		this.primeraVez = primeraVez;
	}
}