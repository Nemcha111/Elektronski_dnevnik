package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import org.springframework.http.ResponseEntity;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OdeljenjeEntity;

public interface OdeljenjeService {
	
	public ResponseEntity<?> proveraIKreiranjeOdeljenja(OdeljenjeEntity odeljenje, Integer idRazreda);

}
