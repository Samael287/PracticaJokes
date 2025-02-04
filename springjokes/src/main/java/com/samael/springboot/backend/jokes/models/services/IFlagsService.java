package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import com.samael.springboot.backend.jokes.models.entidades.Flags;

public interface IFlagsService {
	
	public List<Flags> findAll();
	
	public Flags findById(Integer id);
	
	public Flags save(Flags flag);
	
	public void delete(Integer id);
}
