package com.samael.springboot.backend.jokes.models.entidades;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "primera_vez")
public class PrimeraVez implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "programa", nullable = false)
    private String programa;

    @Column(name = "fecha_emision", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;

    @OneToOne
    @JoinColumn(name = "id_joke", unique = true, nullable = true, referencedColumnName = "id", 
                foreignKey = @ForeignKey(name = "fk_primera_vez_joke"))
    @JsonIgnoreProperties(value = {"primeraVez", "categories", "language", "types", "flagses"}, allowSetters = true)
    private Jokes joke;

    @OneToMany(mappedBy = "primeraVez", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("primeraVez")
    private List<Telefono> telefonos;
    
    /*
      orphanRemoval: Con orphanRemoval = true: Si un Telefono se elimina de la colección en PrimeraVez, 
Hibernate lo eliminará automáticamente de la base de datos.

Sin orphanRemoval: Los Telefonos eliminados de la colección no serán eliminados de la base de datos, 
quedando huérfanos (sin referencia a un PrimeraVez).
     * */
    // Constructor vacío
    public PrimeraVez() {}

    // Constructor con parámetros
    public PrimeraVez(int id, String programa, Date fechaEmision, Jokes joke) {
    	this.id = id;
        this.programa = programa;
        this.fechaEmision = fechaEmision;
        this.joke = joke;
    }

    // Getters y Setters
    public int getId() {
    	return id; 
	}
    public void setId(int id) { 
    	this.id = id; 
	}

    public String getPrograma() { 
    	return programa; 
	}
    public void setPrograma(String programa) { 
    	this.programa = programa; 
	}

    public Date getFechaEmision() { 
    	return fechaEmision; 
	}
    public void setFechaEmision(Date fechaEmision) { 
    	this.fechaEmision = fechaEmision; 
	}

    public Jokes getJoke() { 
    	return joke; 
	}
    public void setJoke(Jokes joke) {
    	this.joke = joke; 
	}

    public List<Telefono> getTelefonos() { 
    	return telefonos; 
	}
    public void setTelefonos(List<Telefono> telefonos) { 
    	this.telefonos = telefonos; 
	}
}
