package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import com.samael.springboot.backend.jokes.models.entidades.PrimeraVez;

public interface IPrimeraVezService {
	
	public List<PrimeraVez> findAll();
	
	public PrimeraVez findById(Integer id);
		
	public PrimeraVez save(PrimeraVez primeraVez);
	
	public void delete(Integer id);

	public PrimeraVez findByJokeId(int id);
}
