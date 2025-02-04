package com.samael.springboot.backend.jokes.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samael.springboot.backend.jokes.models.entidades.Categories;

public interface ICategoriesDAO extends CrudRepository<Categories, Integer>{

}
