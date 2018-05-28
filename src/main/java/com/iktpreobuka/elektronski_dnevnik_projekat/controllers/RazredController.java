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

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RazredRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.RazredService;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/razred")
public class RazredController {

	@Autowired
	private RazredRepository razredRepo;

	@Autowired
	private RazredService razredService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikaziSveRazrede() {

		List<RazredEntity> razredi = new ArrayList<>();

		razredi = (List<RazredEntity>) razredRepo.findAll();

		if (razredi.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan razred."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<RazredEntity>>(razredRepo.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> kreirajRazrede(@Valid @RequestBody RazredEntity razred, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		return razredService.proveraIKreiranjeRazreda(razred);

	}

	@RequestMapping(value = "/obrisi/{idRazreda}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeRazreda(@PathVariable Integer idRazreda) {

		if (razredRepo.findById(idRazreda).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Razred koji zelite da obrisete ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		RazredEntity raz = razredRepo.findById(idRazreda).get();
		if (raz.getOdeljenja().size()>0) {
				return new ResponseEntity<RESTError>(new RESTError("Ovaj razred ima dodeljena odeljenja te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}
		
		

		
		razredRepo.deleteById(idRazreda);
		return new ResponseEntity<RazredEntity>(raz, HttpStatus.OK);

	}

	@RequestMapping(path = "/pojedinacni", method = RequestMethod.POST)
	public ResponseEntity<?> kreiranjePojedinacnogRazreda(@Valid @RequestBody RazredEntity razred,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		// return razredService.proveraIKreiranjePojedniacnogRazreda(razred);
		List<RazredEntity> razredi = (List<RazredEntity>) razredRepo.findAll();

		for (RazredEntity razredEntity : razredi) {

			if (razredEntity.getRazred().equals(razred.getRazred())) {
				if (razredEntity.getSkolskaGodinaRazreda().equals(razred.getSkolskaGodinaRazreda()))

					return new ResponseEntity<RESTError>(
							new RESTError("Razred koji pokusavate da kreirte vec postoji."), HttpStatus.BAD_REQUEST);
			}

		}

		RazredEntity noviRazred = new RazredEntity();
		noviRazred.setRazred(razred.getRazred());
		noviRazred.setSkolskaGodinaRazreda(razred.getSkolskaGodinaRazreda());

		razredRepo.save(noviRazred);

		return new ResponseEntity<RazredEntity>(razredRepo.save(noviRazred), HttpStatus.OK);

	}
	
	
	@RequestMapping(value = "/pronadjiPremaId/{idRazreda}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiRazredPremaId(@PathVariable Integer idRazreda) {

		if (razredRepo.findById(idRazreda).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Razred sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RazredEntity razred = razredRepo.findById(idRazreda).get();
		return new ResponseEntity<RazredEntity>(razred, HttpStatus.OK);

	}
	
	

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	
	
	

}
