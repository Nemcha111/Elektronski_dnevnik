package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.NastavnikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.NastavnikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/nastavnik")
public class NastavnikController {
	
	@Autowired
	private NastavnikRepository nastavnikRepo;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihNastavnika() {

		List<NastavnikEntity> nastavnik = new ArrayList<>();

		nastavnik = (List<NastavnikEntity>) nastavnikRepo.findAll();

		if (nastavnik.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan nastavnik."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<NastavnikEntity>>(nastavnikRepo.findAll(), HttpStatus.OK);

	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogNastavnika(@Valid @RequestBody NastavnikEntity nastavnik, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		NastavnikEntity noviNastavnik = new NastavnikEntity();

		noviNastavnik.setImeNastavnika(nastavnik.getImeNastavnika());
		noviNastavnik.setPrezimeNastavnika(nastavnik.getPrezimeNastavnika());
		noviNastavnik.setKorisnickoImeNastavnika(nastavnik.getKorisnickoImeNastavnika());
		noviNastavnik.setSifraNastavnika(nastavnik.getSifraNastavnika());
		

		return new ResponseEntity<NastavnikEntity>(nastavnikRepo.save(noviNastavnik), HttpStatus.OK);
	}

	
	
	@RequestMapping(value = "/izmena/{idNastavnika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaNastavnika(@Valid @RequestBody NastavnikEntity noviNastavnik,
			@PathVariable Integer idNastavnika, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Nastavnik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();

		nastavnik.setImeNastavnika(noviNastavnik.getImeNastavnika());
		nastavnik.setPrezimeNastavnika(noviNastavnik.getPrezimeNastavnika());
		nastavnik.setKorisnickoImeNastavnika(noviNastavnik.getKorisnickoImeNastavnika());
		

		return new ResponseEntity<NastavnikEntity>(nastavnikRepo.save(nastavnik), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/obrisi/{idNastavnika}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeNastavnika(@PathVariable Integer idNastavnika) {

		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Nastavnik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		NastavnikEntity roditelj = nastavnikRepo.findById(idNastavnika).get();
		if (roditelj.getPredmetniNastavniciOdeljenja().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ovaj nastavnik ima dodeljen predmet ili odeljenje te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}

		nastavnikRepo.deleteById(idNastavnika);
		return new ResponseEntity<NastavnikEntity>(roditelj, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/pronadjiPremaId/{idNastavnika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiNastavnikaPremaId(@PathVariable Integer idNastavnika) {

		
		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Nastavnik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		return new ResponseEntity<NastavnikEntity>(nastavnik, HttpStatus.OK);

	}
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}