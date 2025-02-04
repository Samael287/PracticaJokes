package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import com.samael.springboot.backend.jokes.models.entidades.Jokes;

public interface IJokesService {
	
	public List<Jokes> findAll();
	
	public Jokes findById(Integer id);
	
	public Jokes save(Jokes joke);
	
	public void delete(Integer id);
}
