package com.samael.springboot.backend.jokes.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samael.springboot.backend.jokes.models.entidades.PrimeraVez;

public interface IPrimeraVezDAO extends CrudRepository<PrimeraVez, Integer>{

}
