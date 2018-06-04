package com.iktpreobuka.elektronski_dnevnik_projekat.services;



import org.springframework.http.ResponseEntity;



public interface OcenaService {

	
	public ResponseEntity<?> zakljucivanjeOcenaPolugodisete(String SkolskaGodinaOcene);
	
	public ResponseEntity<?> zakljucivanjeOcenaZavrsnih(String skolskaGodina);
	
}
