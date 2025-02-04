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

import com.samael.springboot.backend.jokes.models.dto.JokesDTO;
import com.samael.springboot.backend.jokes.models.dto.PrimeraVezDTO;
import com.samael.springboot.backend.jokes.models.entidades.Categories;
import com.samael.springboot.backend.jokes.models.entidades.Jokes;
import com.samael.springboot.backend.jokes.models.entidades.Language;
import com.samael.springboot.backend.jokes.models.entidades.Telefono;
import com.samael.springboot.backend.jokes.models.entidades.Types;
import com.samael.springboot.backend.jokes.models.services.ICategoriesService;
import com.samael.springboot.backend.jokes.models.services.IJokesService;
import com.samael.springboot.backend.jokes.models.services.ILanguageService;
import com.samael.springboot.backend.jokes.models.services.ITypesService;


@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class JokesRestController {

	@Autowired
	private IJokesService jokesService;
	
	@Autowired
	private ILanguageService languageService;
	
	@Autowired
	private ICategoriesService categoriesService;
	
	@Autowired
	private ITypesService typesService;
	
	@GetMapping("/jokes")
	public List<JokesDTO> indexto(){
		List<Jokes> jokes = jokesService.findAll();
		List<JokesDTO> jokesDTO = new ArrayList<>();
		
		jokes.forEach(joke ->{
			
			String type = joke.getTypes() != null ?
							joke.getTypes().getType() : "No hay tipo asignado";
			
			String category = joke.getCategories() != null ? 
								joke.getCategories().getCategory() : "No hay categoria asignada";
			
			String language = joke.getLanguage() != null ? 
					joke.getLanguage().getLanguage() : "Sin lenguaje asignado";

			Set<String> flags = joke.getFlagses().stream()
					.map(flag -> flag.getFlag())
							.collect(Collectors.toSet());
			
			JokesDTO jokesdto = new JokesDTO();
			
			jokesdto.setId(joke.getId());
			jokesdto.setText1(joke.getText1());
			jokesdto.setText2(joke.getText2());
			jokesdto.setTypes(type);
			jokesdto.setCategories(category);
			jokesdto.setLanguage(language);
			jokesdto.setFlagses(flags);
			
			jokesDTO.add(jokesdto);
		});
		return jokesDTO;
	}
	
	@GetMapping("/jokes/{id}")
	public ResponseEntity<?> dto(@PathVariable Integer id){
		Jokes joke = null;
		Map<String, Object> respuesta = new HashMap<String, Object>();
	    JokesDTO jokesdto = new JokesDTO();

	    try {
	        joke = jokesService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        respuesta.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        respuesta.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (joke == null) { // Hemos buscado un id que no existe
	        respuesta.put("mensaje", "El identificador buscado no existe");
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	    }

	    jokesdto.setId(joke.getId());
	    jokesdto.setText1(joke.getText1());
		jokesdto.setText2(joke.getText2());
		jokesdto.setTypes(joke.getTypes() != null ? joke.getTypes().getType() : null);
	    jokesdto.setCategories(joke.getCategories() != null ? joke.getCategories().getCategory() : null);
	    jokesdto.setLanguage(joke.getLanguage() != null ? joke.getLanguage().getLanguage() : null);
		// Mapear Flags al DTO
	    Set<String> flagses = joke.getFlagses().stream()
	            .map(flag -> flag.getFlag()) // Obtener el atributo relevante como String
	            .collect(Collectors.toSet());
	    jokesdto.setFlagses(flagses);
	    
	    return new ResponseEntity<JokesDTO>(jokesdto, HttpStatus.OK);
	}
	
	@GetMapping("/jokes/primeravez")
	public List<JokesDTO> listarJokePrimeraVez() {
	    List<Jokes> jokes = jokesService.findAll();
	    List<JokesDTO> jokesDTOList = new ArrayList<>();

	    for (Jokes joke : jokes) {
	        JokesDTO jokesDTO = new JokesDTO();
	        jokesDTO.setId(joke.getId());
	        jokesDTO.setText1(joke.getText1());
	        jokesDTO.setText2(joke.getText2());

	        // 📌 Verificar si las relaciones no son nulas antes de acceder a ellas
	        jokesDTO.setCategories(joke.getCategories() != null ? joke.getCategories().getCategory() : "Sin categoría");
	        jokesDTO.setLanguage(joke.getLanguage() != null ? joke.getLanguage().getLanguage() : "Sin idioma");
	        jokesDTO.setTypes(joke.getTypes() != null ? joke.getTypes().getType() : "Sin tipo");
	        Set<String> flagses = joke.getFlagses().stream()
		            .map(flag -> flag.getFlag()) // Obtener el atributo relevante como String
		            .collect(Collectors.toSet());
		    jokesDTO.setFlagses(flagses);

	        // 📌 Agregar PrimeraVez si existe
	        if (joke.getPrimeraVez() != null) {	    	    
	            PrimeraVezDTO primeraVezDTO = new PrimeraVezDTO();
	            primeraVezDTO.setId(joke.getPrimeraVez().getId());
	            primeraVezDTO.setPrograma(joke.getPrimeraVez().getPrograma());
	            primeraVezDTO.setFechaEmision(joke.getPrimeraVez().getFechaEmision().toString());
	            primeraVezDTO.setJoke_id(joke.getPrimeraVez().getJoke().getId());

	            // 📌 Convertir la lista de teléfonos a `List<String>` en lugar de `List<TelefonoDTO>`
	            List<String> telefonosStringList = joke.getPrimeraVez().getTelefonos().stream()
	                    .map(Telefono::getNumero) // Extrae solo el número como String
	                    .collect(Collectors.toList());

	            primeraVezDTO.setTelefonos(telefonosStringList);
	            jokesDTO.setPrimeraVez(primeraVezDTO);
	        }

	        jokesDTOList.add(jokesDTO);
	    }
	    return jokesDTOList;
	}

	
	@PostMapping("/jokes")
	public ResponseEntity<?> create(@RequestBody Jokes joke, BindingResult result){
		Jokes newJoke = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		 if (joke.getTypes() != null && joke.getTypes().getId() > 0) {
		        Integer typeId = joke.getTypes().getId();

		        if (typeId == 1 && (joke.getText1() == null || joke.getText1().trim().isEmpty())) {
		            response.put("mensaje", "Si el tipo es 1, 'text1' es obligatorio.");
		            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		        }

		        if (typeId == 2 && (
		                (joke.getText1() == null || joke.getText1().trim().isEmpty()) ||
		                (joke.getText2() == null || joke.getText2().trim().isEmpty()))) {
		            response.put("mensaje", "Si el tipo es 2, 'text1' y 'text2' son obligatorios.");
		            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		        }
		    }

		    // 📌 3. Si `type` es null, los textos deben ser null también
		    if (joke.getTypes() == null || joke.getTypes().getId() == 0) {
		        joke.setText1(null);
		        joke.setText2(null);
		    }
		
		try {
			
			// Asegurar que las referencias existen en la base de datos antes de asignarlas
			if (joke.getLanguage() != null) {
			    Integer languageId = joke.getLanguage().getId();
			    if (languageId != null) {
			        Language language = languageService.findById(languageId);
			        joke.setLanguage(language);
			    } else {
			        joke.setLanguage(null);
			    }
			}

			if (joke.getCategories() != null) {
			    Integer categoryId = joke.getCategories().getId();
			    if (categoryId != null) {
			        Categories category = categoriesService.findById(categoryId);
			        joke.setCategories(category);
			    } else {
			        joke.setCategories(null);
			    }
			}

			if (joke.getTypes() != null) {
			    Integer typeId = joke.getTypes().getId();
			    if (typeId != null) {
			        Types type = typesService.findById(typeId);
			        joke.setTypes(type);
			    } else {
			        joke.setTypes(null);
			    }
			}
			
			newJoke = jokesService.save(joke);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El joke ha sido creado con éxito.");
		response.put("joke", newJoke);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/jokes/{id}")
	public ResponseEntity<?> update(@RequestBody Jokes joke, BindingResult result, @PathVariable Integer id){
		Jokes jokeActual = jokesService.findById(id);
		
		Jokes jokeUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(jokeActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el joke ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			jokeActual.setCategories(joke.getCategories());
			jokeActual.setLanguage(joke.getLanguage());
			jokeActual.setText1(joke.getText1());
			jokeActual.setText2(joke.getText2());
			jokeActual.setTypes(joke.getTypes());
			jokeActual.setFlagses(joke.getFlagses());
			
			jokeUpdated = jokesService.save(jokeActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el joke en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El joke ha sido actualizado con éxito.");
		response.put("joke", jokeUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/jokes/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		
		Jokes jokeABorrar = jokesService.findById(id);
		
		Map<String, Object> response = new HashMap<>();

		if(jokeABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, el joke ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
			
		try {
			jokesService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el joke en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El joke ha sido eliminado con éxito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
	
	@GetMapping("/jokes/deleteSoft/{id}")
	public ResponseEntity<?> canDeleteJoke(@PathVariable Integer id){
		
		Jokes jokeABorrar = null;
		
		JokesDTO jokesDto = new JokesDTO();
		
		Map<String, Object> response = new HashMap<>();
		
		try {
	        jokeABorrar = jokesService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        response.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        response.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
		
		if(jokeABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, el joke ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		  Set<String> flagses = jokeABorrar.getFlagses().stream()
		            .map(flag -> flag.getFlag()) // Obtener el atributo relevante como String
		            .collect(Collectors.toSet());
		jokesDto.setFlagses(flagses);
		boolean hasFlags = !flagses.isEmpty();
		
		return new ResponseEntity<>(hasFlags, HttpStatus.OK);		
	}
}
