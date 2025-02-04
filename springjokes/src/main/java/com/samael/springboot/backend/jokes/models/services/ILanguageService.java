package com.samael.springboot.backend.jokes.models.services;

import java.util.List;

import com.samael.springboot.backend.jokes.models.entidades.Language;

public interface ILanguageService {
	
	public List<Language> findAll();

	public Language findById(Integer id);
	
	public Language save(Language language);
	
	public void delete(Integer id);
}
