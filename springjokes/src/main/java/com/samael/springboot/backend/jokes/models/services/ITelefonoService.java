package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import com.samael.springboot.backend.jokes.models.entidades.Telefono;

public interface ITelefonoService {

	public List<Telefono> findAll();
	
	public Telefono findById(Integer id);
	
	public Telefono save(Telefono telefono);
	
	public void delete(Integer id);
}
