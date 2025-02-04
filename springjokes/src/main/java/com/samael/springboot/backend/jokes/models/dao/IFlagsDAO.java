package com.samael.springboot.backend.jokes.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samael.springboot.backend.jokes.models.entidades.Flags;

public interface IFlagsDAO extends CrudRepository<Flags, Integer>{

}
