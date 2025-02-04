package com.samael.springboot.backend.jokes.models.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class JokesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String categories;
	private String language;
	private String types;
	private String text1;
	private String text2;
	private Set<String> flagses = new HashSet<>();
	private PrimeraVezDTO primeraVez;

	public JokesDTO() {
		super();
	}
	
	

	public JokesDTO(int id, String language, String text1) {
		super();
		this.id = id;
		this.language = language;
		this.text1 = text1;
	}

	public JokesDTO(int id, String categories, String language, String types, String text1, String text2,
			Set<String> flagses, PrimeraVezDTO primeraVez) {
		super();
		this.id = id;
		this.categories = categories;
		this.language = language;
		this.types = types;
		this.text1 = text1;
		this.text2 = text2;
		this.flagses = flagses;
		this.primeraVez = primeraVez;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public Set<String> getFlagses() {
		return flagses;
	}

	public void setFlagses(Set<String> flagses) {
		this.flagses = flagses;
	}

	public PrimeraVezDTO getPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(PrimeraVezDTO primeraVez) {
		this.primeraVez = primeraVez;
	}
}
