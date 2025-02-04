package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samael.springboot.backend.jokes.models.dao.IPrimeraVezDAO;
import com.samael.springboot.backend.jokes.models.entidades.PrimeraVez;

@Service
public class PrimeraVezServiceImpl implements IPrimeraVezService{

	@Autowired
	private IPrimeraVezDAO primeraVezDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<PrimeraVez> findAll() {
		return (List<PrimeraVez>) primeraVezDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public PrimeraVez findById(Integer id) {
		return primeraVezDao.findById(id).orElse(null);
	}
	@Override
	@Transactional(readOnly = true)
	public PrimeraVez findByJokeId(int id) {
		return primeraVezDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public PrimeraVez save(PrimeraVez primeraVez) {
		return primeraVezDao.save(primeraVez);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		PrimeraVez primeraVez = findById(id);
	    if (primeraVez != null) {
	        // Romper la relación bidireccional con Jokes
	        if (primeraVez.getJoke() != null) {
	            primeraVez.getJoke().setPrimeraVez(null);
	        }
	        // Forzar la carga de la colección de teléfonos (opcional pero recomendable)
	        primeraVez.getTelefonos().size();
	        // Eliminar la entidad gestionada
	        primeraVezDao.delete(primeraVez);
	        // Si es posible, forzar flush() para comprobar que se ejecuta el DELETE:
	        // primeraVezDao.flush();
	    }
	}
}
