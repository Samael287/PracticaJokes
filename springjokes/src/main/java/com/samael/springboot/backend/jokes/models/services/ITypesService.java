package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import com.samael.springboot.backend.jokes.models.entidades.Types;

public interface ITypesService {

	public List<Types> findAll();
	
	public Types findById(Integer id);
	
	public Types save(Types type);
	
	public void delete(Integer id);
}
