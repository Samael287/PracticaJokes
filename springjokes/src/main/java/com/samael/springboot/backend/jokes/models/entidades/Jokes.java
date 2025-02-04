package com.samael.springboot.backend.jokes.models.entidades;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "jokes")
public class Jokes implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private Categories categories;
	private Language language;
	private Types types;
	private String text1;
	private String text2;
	private Set<Flags> flagses = new HashSet<Flags>(0);
	private PrimeraVez primeraVez;

	public Jokes() {
	}

	public Jokes(int id) {
		this.id = id;
	}

	public Jokes(int id, Categories categories, Language language, Types types, String text1, String text2,
			Set<Flags> flagses, PrimeraVez primeraVez) {
		this.id = id;
		this.categories = categories;
		this.language = language;
		this.types = types;
		this.text1 = text1;
		this.text2 = text2;
		this.flagses = flagses;
		this.primeraVez = primeraVez;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@JsonIgnoreProperties("jokeses")
	public Categories getCategories() {
		return this.categories;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id")
	@JsonIgnoreProperties("jokeses")
	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	@JsonIgnoreProperties("jokeses")
	public Types getTypes() {
		return this.types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	@Column(name = "text1")
	public String getText1() {
		return this.text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	@Column(name = "text2")
	public String getText2() {
		return this.text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "jokes_flags", joinColumns = {
			@JoinColumn(name = "joke_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "flag_id", nullable = false) })
	@JsonIgnoreProperties("jokeses")
	public Set<Flags> getFlagses() {
		return this.flagses;
	}

	public void setFlagses(Set<Flags> flagses) {
		this.flagses = flagses;
	}

	// Relación 1:1 con PrimeraVez
    @OneToOne(mappedBy = "joke", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("joke")
	public PrimeraVez getPrimeraVez() {
		return this.primeraVez;
	}

	public void setPrimeraVez(PrimeraVez primeraVez) {
		this.primeraVez = primeraVez;
	}

	@Override
	public String toString() {
		String flagsRepresentation = flagses.stream().map(Flags::toString) // Convertimos cada objeto Flag a su cadena
																			// representativa
				.collect(Collectors.joining(", ")); // Unimos todas las representaciones en una cadena separada por
													// comas

		return "Jokes [text1=" + text1 + ", text2=" + text2 + ", flagses=" + flagsRepresentation + "]";
	}
}
