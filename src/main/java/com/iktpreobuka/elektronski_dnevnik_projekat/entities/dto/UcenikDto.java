package com.iktpreobuka.elektronski_dnevnik_projekat.entities.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UcenikDto {

	@Column 
	@NotNull(message = "Ime ucenika mora biti uneto.")
	@Size(min=2, max=30, message = "Ime mora biti izmedju {min} i {max} karaktera.")
	private String imeUcenika;

	public UcenikDto() {
		super();
	}
	

	public String getImeUcenika() {
		return imeUcenika;
	}

	public void setImeUcenika(String imeUcenika) {
		this.imeUcenika = imeUcenika;
	}

	@Override
	public String toString() {
		return "UcenikDto [imeUcenika=" + imeUcenika + "]";
	}
	
	
	
	
	
}
