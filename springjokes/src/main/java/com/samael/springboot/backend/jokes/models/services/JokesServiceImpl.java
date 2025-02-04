package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samael.springboot.backend.jokes.models.dao.IJokesDAO;
import com.samael.springboot.backend.jokes.models.entidades.Jokes;

@Service
public class JokesServiceImpl implements IJokesService{

	@Autowired
	private IJokesDAO jokesDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Jokes> findAll() {
		return  (List<Jokes>) jokesDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Jokes findById(Integer id) {
		return jokesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Jokes save(Jokes joke) {
		return jokesDao.save(joke);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		jokesDao.deleteById(id);
	}

}
