package com.samael.springboot.backend.jokes.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samael.springboot.backend.jokes.models.entidades.Telefono;

public interface ITelefonoDAO extends CrudRepository<Telefono, Integer>{

}
