package com.samael.springboot.backend.jokes.models.dto;

import java.util.HashSet;
import java.util.Set;


public class TypesDTO {

	private int id;
	private String type;
	private Set<String> jokeses = new HashSet<>();
	
	public TypesDTO() {
		super();
	}
	
	public TypesDTO(int id, String type, Set<String> jokeses) {
		super();
		this.id = id;
		this.type = type;
		this.jokeses = jokeses;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Set<String> getJokeses() {
		return jokeses;
	}
	
	public void setJokeses(Set<String> jokeses) {
		this.jokeses = jokeses;
	}
}
