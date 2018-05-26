package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;

public interface RazredService {

	public ResponseEntity<?> proveraIKreiranjeRazreda(RazredEntity razred);
	
	public ResponseEntity<?> proveraIKreiranjePojedniacnogRazreda(RazredEntity razred);

}
