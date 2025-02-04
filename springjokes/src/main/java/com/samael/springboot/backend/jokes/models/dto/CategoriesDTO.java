package com.samael.springboot.backend.jokes.models.dto;

import java.util.HashSet;
import java.util.Set;

public class CategoriesDTO {
	private int id;
	private String category;
	private Set<String> jokeses = new HashSet<>();
	
	public CategoriesDTO() {
		super();
	}
	
	public CategoriesDTO(int id, String category, Set<String> jokeses) {
		super();
		this.id = id;
		this.category = category;
		this.jokeses = jokeses;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Set<String> getJokeses() {
		return jokeses;
	}
	
	public void setJokeses(Set<String> jokeses) {
		this.jokeses = jokeses;
	}
}
