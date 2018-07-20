package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RazredRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.OdeljenjeService;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/odeljenje")
public class OdeljenjeController {

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	@Autowired
	public RazredRepository razredRepo;

	@Autowired
	public OdeljenjeService odeljenjeService;
	
	@Autowired
	public UcenikRepository ucenikRepo;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikaziSvaOdeljenja() {

		List<OdeljenjeEntity> odeljenja = new ArrayList<>();

		odeljenja = (List<OdeljenjeEntity>) odeljenjeRepo.findAll();

		if (odeljenja.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjeno ni jedano odeljenje."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OdeljenjeEntity>>(odeljenjeRepo.findAll(), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/razred-id/{idRazreda}", method = RequestMethod.POST)
	public ResponseEntity<?> kreirajeOdeljenja(@Valid @RequestBody OdeljenjeEntity odeljenje,
			@PathVariable Integer idRazreda, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		return odeljenjeService.proveraIKreiranjeOdeljenja(odeljenje, idRazreda);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/izmena/{idOdeljenja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaPredmeta(@Valid @RequestBody OdeljenjeEntity novoOdeljenje,
			@PathVariable Integer idOdeljenja, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ocena sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();

		odeljenje.setImeOdeljenja(novoOdeljenje.getImeOdeljenja());

		return new ResponseEntity<OdeljenjeEntity>(odeljenjeRepo.save(odeljenje), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/obrisi/{idOdeljenja}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeOdeljenja(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Odeljenje koje zelite da obrisete ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		if (odeljenje.getUcenici().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ovo odeljenje ima dodeljenog bar jednog ucenika te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}

		odeljenjeRepo.deleteById(idOdeljenja);
		return new ResponseEntity<OdeljenjeEntity>(odeljenje, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/pronadjiPremaId/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiOdeljenjePremaId(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Odeljenje sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		return new ResponseEntity<OdeljenjeEntity>(odeljenje, HttpStatus.OK);

	}

	
	@RequestMapping(value = "/prikazUcenikaOdeljenja/idOdeljenja/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> prikazUcenikaOdeljenja(@PathVariable Integer idOdeljenja) {
		
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		
		List<UcenikEntity> sviUcenici = (List<UcenikEntity>)ucenikRepo.findAll();
		
		List<UcenikEntity> uceniciRazreda = new ArrayList<>();
		
		for (UcenikEntity ucenikEntity : sviUcenici) {
			if (ucenikEntity.getOdeljenjeUcenika().equals(odeljenje)) {
				
				uceniciRazreda.add(ucenikEntity);
			}	
		}
		
		return new ResponseEntity<Iterable<UcenikEntity>>(uceniciRazreda, HttpStatus.OK);
			
	}

	

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
