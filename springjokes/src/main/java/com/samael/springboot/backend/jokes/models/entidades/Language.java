package com.samael.springboot.backend.jokes.models.entidades;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "language")

public class Language implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String code;
	private String language;
	private Set<Jokes> jokeses = new HashSet<Jokes>(0);

	public Language() {
	}

	public Language(int id) {
		this.id = id;
	}

	public Language(int id, String code, String language, Set<Jokes> jokeses) {
		this.id = id;
		this.code = code;
		this.language = language;
		this.jokeses = jokeses;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Usa AUTO o SEQUENCE según la base de datos
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotEmpty(message = "El código del language no puede estar en blanco.")
	@Column(name = "code", length = 2, nullable = false)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@NotEmpty(message = "El language no puede estar en blanco.")
	@Column(name = "language", nullable = false)
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Jokes> getJokeses() {
		return this.jokeses;
	}

	public void setJokeses(Set<Jokes> jokeses) {
		this.jokeses = jokeses;
	}

	@Override
	public String toString() {
		return "Language [id=" + id + ", code=" + code + ", language=" + language + "]";
	}
}
