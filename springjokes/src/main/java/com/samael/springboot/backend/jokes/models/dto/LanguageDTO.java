package com.samael.springboot.backend.jokes.models.dto;

import java.util.HashSet;
import java.util.Set;

public class LanguageDTO {
	private int id;
	private String code;
	private String language;
	private Set<String> jokeses = new HashSet<>();
	public LanguageDTO() {
		super();
	}

	public LanguageDTO(int id, String code, String language, Set<String> jokeses) {
		super();
		this.id = id;
		this.code = code;
		this.language = language;
		this.jokeses = jokeses;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public Set<String> getJokeses() {
		return jokeses;
	}
	
	public void setJokeses(Set<String> jokeses) {
		this.jokeses = jokeses;
	}
}
