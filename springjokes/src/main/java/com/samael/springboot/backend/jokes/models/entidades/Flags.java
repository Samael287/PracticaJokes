package com.samael.springboot.backend.jokes.models.entidades;

import java.util.HashSet;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "flags")
public class Flags implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String flag;
	private Set<Jokes> jokeses = new HashSet<Jokes>(0);

	public Flags() {
	}

	public Flags(int id, String flag) {
		this.id = id;
		this.flag = flag;
	}

	public Flags(int id, String flag, Set<Jokes> jokeses) {
		this.id = id;
		this.flag = flag;
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

	@Column(name = "flag", nullable = false)
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "jokes_flags", joinColumns = {
			@JoinColumn(name = "flag_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "joke_id", nullable = false) })
	@JsonIgnoreProperties("flagses")
	public Set<Jokes> getJokeses() {
		return this.jokeses;
	}

	public void setJokeses(Set<Jokes> jokeses) {
		this.jokeses = jokeses;
	}

	@Override
	public String toString() {
		return  this.flag;
	}
}
