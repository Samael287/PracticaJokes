package com.samael.springboot.backend.jokes.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.samael.springboot.backend.jokes.models.dto.TelefonoDTO;
import com.samael.springboot.backend.jokes.models.entidades.PrimeraVez;
import com.samael.springboot.backend.jokes.models.entidades.Telefono;
import com.samael.springboot.backend.jokes.models.services.IPrimeraVezService;
import com.samael.springboot.backend.jokes.models.services.ITelefonoService;

@CrossOrigin({"*"})
@RestController
@RequestMapping("/api")
public class TelefonoRestController {
	
	@Autowired
	private ITelefonoService telefonoService;
	
	@Autowired
	private IPrimeraVezService primeraVezService;
	
	@GetMapping("/telefonos")
	public List<TelefonoDTO> indexto(){
		List<Telefono> telefonos = telefonoService.findAll();
		List<TelefonoDTO> telefonosDTO = new ArrayList<>();
		
		telefonos.forEach(telf ->{
			
			String numero = telf.getNumero() != null ? 
					telf.getNumero() : "Sin número asignados";
			
			TelefonoDTO telefonodto = new TelefonoDTO();
			
			telefonodto.setId(telf.getId());
			telefonodto.setNumero(numero);
			telefonodto.setPrimeraVez_id(telf.getPrimeraVez().getId());
			
			telefonosDTO.add(telefonodto);
			
		});
		return telefonosDTO;
	}
	
	@GetMapping("/telefonos/{id}")
	public ResponseEntity<?> dto(@PathVariable Integer id){
		Telefono telefono = null;
		Map<String, Object> respuesta = new HashMap<String, Object>();
	    TelefonoDTO telefonodto = new TelefonoDTO();

	    try {
	    	telefono = telefonoService.findById(id);
	    } catch (Exception e) { // Base de datos inaccesible
	        respuesta.put("mensaje", "Error al realizar la consulta sobre la base de datos");
	        respuesta.put("error", e.getMessage().concat(": ").concat(e.getStackTrace().toString()));
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (telefono == null) { // Hemos buscado un id que no existe
	        respuesta.put("mensaje", "El identificador buscado no existe");
	        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
	    }

	    telefonodto.setId(telefono.getId());
	    telefonodto.setNumero(telefono.getNumero());
	    telefonodto.setPrimeraVez_id(telefono.getPrimeraVez().getId());
		
	    
	    return new ResponseEntity<TelefonoDTO>(telefonodto, HttpStatus.OK);
	}
	
	@PostMapping("/telefonos")
	public ResponseEntity<?> create(@RequestBody Telefono telefono, BindingResult result){
		Telefono newTelefono = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (telefono.getPrimeraVez() == null || telefono.getPrimeraVez().getId() == 0) {
	        response.put("mensaje", "Debe proporcionar un ID válido de PrimeraVez.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
		
	    PrimeraVez primeraVez = primeraVezService.findById(telefono.getPrimeraVez().getId());
	    telefono.setPrimeraVez(primeraVez);

		try {
			newTelefono = telefonoService.save(telefono);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El telefono ha sido creado con éxito.");
		response.put("telefono", newTelefono);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/telefonos/{id}")
	public ResponseEntity<?> update(@RequestBody Telefono telefono, BindingResult result, @PathVariable Integer id){
		Telefono telefonoActual = telefonoService.findById(id);
		
		Telefono telefonoUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" +err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(telefonoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el telefono ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			telefonoActual.setNumero(telefono.getNumero());
			telefonoActual.setPrimeraVez(telefono.getPrimeraVez());
			
			telefonoUpdated = telefonoService.save(telefonoActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el telefono en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// En lugar de devolver directamente la entidad, armar un DTO
		TelefonoDTO dto = new TelefonoDTO();
	    dto.setId(telefonoUpdated.getId());
	    dto.setNumero(telefonoUpdated.getNumero());
	    dto.setPrimeraVez_id(telefonoUpdated.getPrimeraVez().getId());
	    
	    response.put("mensaje", "El telefono ha sido actualizado con éxito.");
	    response.put("telefono", dto);  // aquí devuelves el DTO, no la entidad

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/telefonos/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		
		Telefono telefonoABorrar = telefonoService.findById(id);
		
		Map<String, Object> response = new HashMap<>();

		if(telefonoABorrar == null) {
			response.put("mensaje", "Error: no se pudo borrar, el telefono ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
			
		try {
			telefonoService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el telefono en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El telefono ha sido eliminado con éxito.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
}
