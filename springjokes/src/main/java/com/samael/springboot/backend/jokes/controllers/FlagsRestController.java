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

import com.samael.springboot.backend.jokes.models.dto.FlagsDTO;
import com.samael.springboot.backend.jokes.models.entidades.Flags;
import com.samael.springboot.backend.jokes.models.services.IFlagsService;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class FlagsRestController {
	
	@Autowired
	private IFlagsService flagsService;
	
	@GetMapping("/flags")
	public List<FlagsDTO> indexto(){
		List<Flags> flags = flagsService.findAll();
		List<FlagsDTO> flagsDTO = new ArrayList<>();
		
		flags.forEach(flag ->{
			
			String flagName = flag.getFlag() != null ? 
					flag.getFlag() : "Sin flags asignados";

			Set<String> jokes = flag.getJokeses().stream()
					.map(joke -> "Text 1: " + joke.getText1() + 
							" Text 2: " + joke.getText2())
							.collect(Collectors.toSet());
			
			FlagsDTO flagsdto = new FlagsDTO();
			
			flagsdto.setId(flag.getId());
			flagsdto.setFlag(flagName);
			flagsdto.setJokeses(jokes);
			
			flagsDTO.add(flagsdto);
		});
		return flagsDTO;
	}
	
	@GetMapping("/flags/{id}")
	public ResponseEntity<?> dto(@PathVariable Integer id){
		Flags flag = null;
		Map<String, Object> respuesta = new HashMap<String, Object>();
	    FlagsDTO flagsdto = new FlagsDTO();

	    try {
	    	flag = flagsService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        respuesta.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        respuesta.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (flag == null) { // Hemos buscado un id que no existe
	        respuesta.put("mensaje", "El identificador buscado no existe");
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	    }

	    flagsdto.setId(flag.getId());
	    flagsdto.setFlag(flag.getFlag());
	    Set<String> jokes = flag.getJokeses().stream()
				.map(joke -> "Text 1: " + joke.getText1() + 
						" Text 2: " + joke.getText2())
						.collect(Collectors.toSet());
	    flagsdto.setJokeses(jokes);
	    
	    return new ResponseEntity<FlagsDTO>(flagsdto, HttpStatus.OK);
	}
	
	@GetMapping("/jokesflags")
	public List<FlagsDTO> getFlagsWithJokes() {
	    List<Flags> flagsList = flagsService.findAll();

	    return flagsList.stream().map(flag -> {
	        Set<String> jokesStringList = flag.getJokeses().stream()
	                .map(joke -> {
	                    String language = (joke.getLanguage() != null) ? joke.getLanguage().getLanguage() : "Sin idioma";
	                    return "ID: " + joke.getId() + ", Texto1: " + (joke.getText1() != null ? joke.getText1() : "Sin texto") + ", Idioma: " + language;
	                })
	                .collect(Collectors.toSet());

	        return new FlagsDTO(flag.getId(), flag.getFlag(), jokesStringList);
	    }).collect(Collectors.toList());
	}


	
	@PostMapping("/flags") //mostrar flags solo? o tambien jokes asociados?
	public ResponseEntity<?> create(@RequestBody Flags flag, BindingResult result){
		Flags newFlag = null;
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
			newFlag = flagsService.save(flag);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El flag ha sido creado con éxito.");
		response.put("flag", newFlag);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/flags/{id}")
	public ResponseEntity<?> update(@RequestBody Flags flag, BindingResult result, @PathVariable Integer id){
		Flags flagActual = flagsService.findById(id);
		
		Flags flagUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(flagActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el flag ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			flagActual.setFlag(flag.getFlag());
			
			flagUpdated = flagsService.save(flagActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el flag en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// En lugar de devolver directamente la entidad, armar un DTO
	    FlagsDTO dto = new FlagsDTO();
	    dto.setId(flagUpdated.getId());
	    dto.setFlag(flagUpdated.getFlag());
	    
	    // Si quisieras devolver los chistes asociados como Strings, por ejemplo:
	    Set<String> jokes = flagUpdated.getJokeses().stream()
				.map(joke -> "Text 1: " + joke.getText1() + 
						" Text 2: " + joke.getText2())
						.collect(Collectors.toSet());
	    dto.setJokeses(jokes);
	    
	    response.put("mensaje", "El flag ha sido actualizado con éxito.");
	    response.put("flag", dto);  // aquí devuelves el DTO, no la entidad

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/flags/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		
		Flags flagABorrar = flagsService.findById(id);
		
		Map<String, Object> response = new HashMap<>();

		if(flagABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, el flag ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
			
		try {
			flagsService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el flag en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El flag ha sido eliminado con éxito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
}
