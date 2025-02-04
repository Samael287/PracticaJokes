package com.samael.springboot.backend.jokes.controllers;

import java.util.ArrayList;
import java.util.Date;
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

import com.samael.springboot.backend.jokes.models.dto.PrimeraVezDTO;
import com.samael.springboot.backend.jokes.models.entidades.Jokes;
import com.samael.springboot.backend.jokes.models.entidades.PrimeraVez;
import com.samael.springboot.backend.jokes.models.entidades.Telefono;
import com.samael.springboot.backend.jokes.models.services.IJokesService;
import com.samael.springboot.backend.jokes.models.services.IPrimeraVezService;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class PrimeraVezRestController {

	@Autowired
	private IPrimeraVezService primeraVezService;
	
	@Autowired
	private IJokesService jokesService; 
	
	@GetMapping("/primeravez")
	public List<PrimeraVezDTO> indexto(){
		List<PrimeraVez> primerasVeces = primeraVezService.findAll();
		List<PrimeraVezDTO> primerasVecesDTO = new ArrayList<>();
		
		primerasVeces.forEach(primeraVez ->{
			
			String programa = primeraVez.getPrograma() != null ? 
					primeraVez.getPrograma() : "Sin programa asignados";
			
			String fechaEmision = primeraVez.getFechaEmision() != null ?
					primeraVez.getFechaEmision().toString() : "Sin fecha de emision";

			List<String> telefonos = primeraVez.getTelefonos().stream()
					.map(telefono -> /*"Teléfonos: " +*/ telefono.getNumero())
					.collect(Collectors.toList());
			
			PrimeraVezDTO primeravezdto = new PrimeraVezDTO();
			
			primeravezdto.setId(primeraVez.getId());
			primeravezdto.setPrograma(programa);
			primeravezdto.setFechaEmision(fechaEmision);
			primeravezdto.setJoke_id(primeraVez.getJoke().getId());
			primeravezdto.setTelefonos(telefonos);
			
			primerasVecesDTO.add(primeravezdto);
		});
		return primerasVecesDTO;
	}
	
	@GetMapping("/primeravez/{id}")
	public ResponseEntity<?> dto(@PathVariable Integer id){
		PrimeraVez primeraVez = null;
		Map<String, Object> respuesta = new HashMap<String, Object>();
		PrimeraVezDTO primeravezdto = new PrimeraVezDTO();

	    try {
	    	primeraVez = primeraVezService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        respuesta.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        respuesta.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (primeraVez == null) { // Hemos buscado un id que no existe
	        respuesta.put("mensaje", "El identificador buscado no existe");
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	    }

	    primeravezdto.setId(primeraVez.getId());
	    primeravezdto.setPrograma(primeraVez.getPrograma());
	    primeravezdto.setFechaEmision(primeraVez.getFechaEmision().toString());
	    primeravezdto.setJoke_id(primeraVez.getJoke().getId());
	    //Mapear telefono al DTO
	    List<String> telefonos = primeraVez.getTelefonos().stream()
				.map(telefono -> "Teléfonos: " + telefono.getNumero())
				.collect(Collectors.toList());
	    
	    primeravezdto.setTelefonos(telefonos);
	    
	    return new ResponseEntity<PrimeraVezDTO>(primeravezdto, HttpStatus.OK);
	}
	
	@PostMapping("/primeravez")//corregirlo. eoicursosfondosocialeuropeo
	public ResponseEntity<?> create(@RequestBody PrimeraVez primeraVez, BindingResult result){
		PrimeraVez newPrimeraVez = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (primeraVez.getJoke().getId() <= 0) {
	        response.put("mensaje", "Debe proporcionar un ID válido para el joke.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
		
		Date fechaActual = new Date(); // 📌 Fecha actual en formato Date
	    if (primeraVez.getFechaEmision() != null && primeraVez.getFechaEmision().after(fechaActual)) {
	        response.put("mensaje", "La fecha de emisión no puede ser mayor a la fecha actual.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

		
	    Jokes joke = jokesService.findById(primeraVez.getJoke().getId());
	    
	    if (joke == null) {
	        response.put("mensaje", "El chiste con ID " + primeraVez.getJoke().getId() + " no existe.");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    // 📌 4. Asignar el chiste a la primera vez
	    primeraVez.setJoke(joke);
		
		 // Verificar que hay al menos un teléfono asociado
	    if (primeraVez.getTelefonos() == null || primeraVez.getTelefonos().isEmpty()) {
	        response.put("mensaje", "Debe incluir al menos un teléfono.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    // 🚀 SOLUCIÓN: Asignar `PrimeraVez` a cada `Telefono`
	    for (Telefono telefono : primeraVez.getTelefonos()) {
	        telefono.setPrimeraVez(primeraVez);
	    }
		
		try {
			newPrimeraVez = primeraVezService.save(primeraVez);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La primera vez ha sido creada con éxito.");
		response.put("primera vez", newPrimeraVez);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/primeravez/{id}")
	public ResponseEntity<?> update(@RequestBody PrimeraVez primeraVez, BindingResult result, @PathVariable Integer id){
		PrimeraVez primeraVezActual = primeraVezService.findById(id);
		
		PrimeraVez primeraVezUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(primeraVezActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la primera vez ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Date fechaActual = new Date(); // 📌 Fecha actual en formato Date
	    if (primeraVez.getFechaEmision() != null && primeraVez.getFechaEmision().after(fechaActual)) {
	        response.put("mensaje", "La fecha de emisión no puede ser mayor a la fecha actual.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
		
		try {
			primeraVezActual.setPrograma(primeraVez.getPrograma());
			primeraVezActual.setFechaEmision(primeraVez.getFechaEmision());
			
			if (primeraVez.getJoke().getId() <= 0) {
		        response.put("mensaje", "Debe proporcionar un ID válido para el joke.");
		        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		    }
			
			Jokes joke = jokesService.findById(primeraVez.getJoke().getId());
		    	if (joke == null) {
		            response.put("mensaje", "El chiste con ID " + primeraVez.getJoke().getId() + " no existe.");
		            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		        }
			primeraVezActual.setJoke(joke);
			
			Set<String> numerosNuevos = primeraVez.getTelefonos().stream()
		                .map(Telefono::getNumero)
		                .collect(Collectors.toSet());

	        // Filtrar y eliminar los teléfonos que ya no están en la nueva lista
	        primeraVezActual.getTelefonos().removeIf(t -> !numerosNuevos.contains(t.getNumero()));

			
			 // - Si el número ya existe, no lo agregamos nuevamente.
	        // - Si es un número nuevo, lo agregamos correctamente.
	        Set<String> numerosExistentes = primeraVezActual.getTelefonos().stream()
	                .map(Telefono::getNumero)
	                .collect(Collectors.toSet());

	        for (Telefono telefono : primeraVez.getTelefonos()) {
	            if (!numerosExistentes.contains(telefono.getNumero())) { // Solo agrega si el número es nuevo
	                telefono.setPrimeraVez(primeraVezActual);
	                primeraVezActual.getTelefonos().add(telefono);
	            }
	        }
	       			
			primeraVezUpdated = primeraVezService.save(primeraVezActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el joke en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		PrimeraVezDTO dto = new PrimeraVezDTO();
		
		dto.setId(primeraVezUpdated.getId());
	    dto.setPrograma(primeraVezUpdated.getPrograma());
	    dto.setFechaEmision(primeraVezUpdated.getFechaEmision().toString());
	    dto.setJoke_id(primeraVezUpdated.getJoke().getId());
	    
	    List<String> telefonos = primeraVez.getTelefonos().stream()
				.map(telefono -> "Teléfonos: " + telefono.getNumero())
				.collect(Collectors.toList());

	    dto.setTelefonos(telefonos);
	    
		response.put("mensaje", "La primera vez ha sido actualizada con éxito.");
		response.put("primera vez", primeraVezUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/primeravez/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		
		PrimeraVez vezABorrar = primeraVezService.findById(id);
		
		Map<String, Object> response = new HashMap<>();

		if(vezABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, la vez ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
			
		try {
			primeraVezService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar la primera vez en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La primera vez ha sido eliminado con éxito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
}
