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

import com.samael.springboot.backend.jokes.models.dto.TypesDTO;
import com.samael.springboot.backend.jokes.models.entidades.Types;
import com.samael.springboot.backend.jokes.models.services.ITypesService;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class TypesRestController {
	
	@Autowired
	private ITypesService typesService;
	
	@GetMapping("/types")
	public List<TypesDTO> indexto(){
		List<Types> types = typesService.findAll();
		List<TypesDTO> typesDTO = new ArrayList<>();
		
		types.forEach(type ->{
			
			String typeName = type.getType() != null ?
					type.getType() : "Sin type asignado";
			
			Set<String> jokes = type.getJokeses().stream()
					.map(joke -> "Text 1: " + joke.getText1() + 
							" Text 2: " + joke.getText2())
							.collect(Collectors.toSet());
			
			TypesDTO typesdto = new TypesDTO();
			
			typesdto.setId(type.getId());
			typesdto.setType(typeName);
			typesdto.setJokeses(jokes);
			
			typesDTO.add(typesdto);
		});
		return typesDTO;
	}
	
	@GetMapping("/types/{id}")
	public ResponseEntity<?> dto(@PathVariable Integer id){
		Types type = null;
		Map<String, Object> respuesta = new HashMap<String, Object>();
		TypesDTO typesdto = new TypesDTO();

	    try {
	    	type = typesService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        respuesta.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        respuesta.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (type == null) { // Hemos buscado un id que no existe
	        respuesta.put("mensaje", "El identificador buscado no existe");
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	    }

	    typesdto.setId(type.getId());
	    typesdto.setType(type.getType());
	    Set<String> jokes = type.getJokeses().stream()
				.map(joke -> "Text 1: " + joke.getText1() + 
						" Text 2: " + joke.getText2())
						.collect(Collectors.toSet());
	    typesdto.setJokeses(jokes);
	    
	    return new ResponseEntity<TypesDTO>(typesdto, HttpStatus.OK);
	}
	
	@PostMapping("/types") //hace falta? solo son null, single o twoparts?
	public ResponseEntity<?> create(@RequestBody Types type, BindingResult result){
		Types newType = null;
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
			newType = typesService.save(type);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El type ha sido creada con éxito.");
		response.put("type", newType);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/types/{id}")
	public ResponseEntity<?> update(@RequestBody Types type, BindingResult result, @PathVariable Integer id){
		Types typesActual = typesService.findById(id);
		
		Types typesUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(typesActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el type ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			typesActual.setType(type.getType());
			
			typesUpdated = typesService.save(typesActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el type en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El type ha sido actualizado con éxito.");
		response.put("types", typesUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/types/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		
		Types typeABorrar = typesService.findById(id);
		
		Map<String, Object> response = new HashMap<>();

		if(typeABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, el type ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
			
		try {
			typesService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el type en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "el type ha sido eliminado con éxito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
}
