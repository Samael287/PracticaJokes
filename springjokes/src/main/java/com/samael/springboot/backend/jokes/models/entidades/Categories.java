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
@Table(name = "categories")
public class Categories implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String category;
	private Set<Jokes> jokeses = new HashSet<Jokes>(0);

	public Categories() {
	}

	public Categories(int id, String category) {
		this.id = id;
		this.category = category;
	}

	public Categories(int id, String category, Set<Jokes> jokeses) {
		this.id = id;
		this.category = category;
		this.jokeses = jokeses;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usa auto-increment
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@NotEmpty(message = "El nombre de la categoría no puede estar vacío")
	@Column(name = "category", nullable = false)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categories", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Jokes> getJokeses() {
		return this.jokeses;
	}

	public void setJokeses(Set<Jokes> jokeses) {
		this.jokeses = jokeses;
	}

	@Override
	public String toString() {
		return "Categories [id=" + id + ", category=" + category + "]";
	}
}
