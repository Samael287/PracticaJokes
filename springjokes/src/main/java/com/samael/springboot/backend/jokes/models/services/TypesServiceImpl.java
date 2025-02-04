package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samael.springboot.backend.jokes.models.dao.ITypesDAO;
import com.samael.springboot.backend.jokes.models.entidades.Types;

@Service
public class TypesServiceImpl implements ITypesService{

	@Autowired
	private ITypesDAO typesDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Types> findAll() {
		return (List<Types>) typesDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Types findById(Integer id) {
		return typesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Types save(Types type) {
		return typesDao.save(type);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		typesDao.deleteById(id);
	}
}
