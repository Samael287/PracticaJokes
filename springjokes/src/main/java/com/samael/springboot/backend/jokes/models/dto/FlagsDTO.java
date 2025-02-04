package com.samael.springboot.backend.jokes.models.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class FlagsDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String flag;
	private Set<String> jokeses = new HashSet<>();
	
	public FlagsDTO() {
		super();
	}
	
	public FlagsDTO(int id, String flag, Set<String> jokeses) {
		super();
		this.id = id;
		this.flag = flag;
		this.jokeses = jokeses;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public Set<String> getJokeses() {
		return jokeses;
	}
	
	public void setJokeses(Set<String> jokeses) {
		this.jokeses = jokeses;
	}
}
