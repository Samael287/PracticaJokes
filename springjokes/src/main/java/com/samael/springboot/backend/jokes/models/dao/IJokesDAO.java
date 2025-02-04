package com.samael.springboot.backend.jokes.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samael.springboot.backend.jokes.models.entidades.Jokes;

public interface IJokesDAO extends CrudRepository<Jokes, Integer>{

}
