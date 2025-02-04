package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samael.springboot.backend.jokes.models.dao.ITelefonoDAO;
import com.samael.springboot.backend.jokes.models.entidades.Telefono;

@Service
public class TelefonoServiceImpl implements ITelefonoService{

	@Autowired
	private ITelefonoDAO telefonoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Telefono> findAll() {
		return (List<Telefono>) telefonoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Telefono findById(Integer id) {
		return telefonoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Telefono save(Telefono telefono) {
		return telefonoDao.save(telefono);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		telefonoDao.deleteById(id);
	}
}
