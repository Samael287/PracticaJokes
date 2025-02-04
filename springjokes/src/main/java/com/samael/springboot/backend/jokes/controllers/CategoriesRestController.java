package com.samael.springboot.backend.jokes.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samael.springboot.backend.jokes.models.dto.CategoriesDTO;
import com.samael.springboot.backend.jokes.models.entidades.Categories;
import com.samael.springboot.backend.jokes.models.services.ICategoriesService;

import jakarta.validation.Valid;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class CategoriesRestController {
	
	@Autowired
	private ICategoriesService categoriesService;
	
	@GetMapping("/categories")
	public List<CategoriesDTO> indexto(){
		List<Categories> categories = categoriesService.findAll();
		List<CategoriesDTO> categoriesDTO = new ArrayList<>();
		
		categories.forEach(category ->{
			
			String categoryName = category.getCategory() != null ?
						category.getCategory() : "Sin category asignado";
			
			Set<String> jokes = category.getJokeses().stream()
					.map(joke -> "Text 1: " + joke.getText1() + 
							" Text 2: " + joke.getText2())
							.collect(Collectors.toSet());
			
			CategoriesDTO categoriesdto = new CategoriesDTO();
			
			categoriesdto.setId(category.getId());
			categoriesdto.setCategory(categoryName);
			categoriesdto.setJokeses(jokes);
			
			categoriesDTO.add(categoriesdto);
		});
		return categoriesDTO;
	}
	
	@GetMapping("/categories/{id}")
	public ResponseEntity<?> dto(@PathVariable Integer id){
		Categories category = null;
		Map<String, Object> respuesta = new HashMap<String, Object>();
		CategoriesDTO categoriesdto = new CategoriesDTO();

	    try {
	    	category = categoriesService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        respuesta.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        respuesta.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (category == null) { // Hemos buscado un id que no existe
	        respuesta.put("mensaje", "El identificador buscado no existe");
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	    }

	    categoriesdto.setId(category.getId());
	    categoriesdto.setCategory(category.getCategory());
	    Set<String> jokes = category.getJokeses().stream()
				.map(joke -> "Text 1: " + joke.getText1() + 
						" Text 2: " + joke.getText2())
						.collect(Collectors.toSet());
	    categoriesdto.setJokeses(jokes);
	    
	    return new ResponseEntity<CategoriesDTO>(categoriesdto, HttpStatus.OK);
	}
	
	@PostMapping("/categories")
	public ResponseEntity<?> create(@RequestBody @Valid Categories category, BindingResult result){
		Categories newCategory = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newCategory = categoriesService.save(category);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La category ha sido creada con éxito.");
		response.put("category", newCategory);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/categories/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid Categories category, BindingResult result, @PathVariable Integer id){
		Categories categoryActual = categoriesService.findById(id);
		
		Categories categoryUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(categoryActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la category ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			categoryActual.setCategory(category.getCategory());
			
			categoryUpdated = categoriesService.save(categoryActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar la category en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La category ha sido actualizado con éxito.");
		response.put("category", categoryUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		
		Categories categoryABorrar = categoriesService.findById(id);
		
		Map<String, Object> response = new HashMap<>();

		if(categoryABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, la category ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
			
		try {
			categoriesService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar la category en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "la category ha sido eliminada con éxito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
}
