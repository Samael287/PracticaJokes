package com.samael.springboot.backend.jokes.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samael.springboot.backend.jokes.models.entidades.Types;

public interface ITypesDAO extends CrudRepository<Types, Integer>{

}
