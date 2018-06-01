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

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredmetEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.PredmetRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RazredRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/predmet")
public class PredmetController {

	@Autowired
	public PredmetRepository predmetRepo;

	@Autowired
	public RazredRepository razredRepo;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikaziSvePredmete() {

		List<PredmetEntity> predmet = new ArrayList<>();

		predmet = (List<PredmetEntity>) predmetRepo.findAll();

		if (predmet.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjeno ni jedan predmet."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<PredmetEntity>>(predmetRepo.findAll(), HttpStatus.OK);

	}

	@RequestMapping(value = "/razred/{idRazreda}", method = RequestMethod.POST)
	public ResponseEntity<?> kreirajePredmeta(@Valid @RequestBody PredmetEntity predmet,
			@PathVariable Integer idRazreda, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (razredRepo.findById(idRazreda).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Razred sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		List<PredmetEntity> predmeti = (List<PredmetEntity>) predmetRepo.findAll();

		for (PredmetEntity predmetEntity : predmeti) {

			if (predmetEntity.getImePredmeta().equals(predmet.getImePredmeta())) {

				return new ResponseEntity<RESTError>(new RESTError("Predmet koji pokusavate da kreirte vec postoji."),
						HttpStatus.BAD_REQUEST);
			}

		}

		PredmetEntity noviPredmet = new PredmetEntity();
		RazredEntity razred = razredRepo.findById(idRazreda).get();

		noviPredmet.setImePredmeta(predmet.getImePredmeta());
		noviPredmet.setNedeljniFondCasova(predmet.getNedeljniFondCasova());
		noviPredmet.setRazred(razred);

		return new ResponseEntity<PredmetEntity>(predmetRepo.save(noviPredmet), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/izmena/{idPredmeta}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaPredmeta(@Valid @RequestBody PredmetEntity noviPredmet,
			@PathVariable Integer idPredmeta, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Predmet sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();

		predmet.setImePredmeta(noviPredmet.getImePredmeta());
		predmet.setNedeljniFondCasova(noviPredmet.getNedeljniFondCasova());

		

		return new ResponseEntity<PredmetEntity>(predmetRepo.save(predmet), HttpStatus.OK);
	}

	

	@RequestMapping(value = "/obrisi/{idPredmeta}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjePredmet(@PathVariable Integer idPredmeta) {

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Predmet koji zelite da obrisete ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		if (predmet.getPredmetniNastavnici().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ovaj predmet ima dodeljenog predmetnog nastavnika te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}

		predmetRepo.deleteById(idPredmeta);
		return new ResponseEntity<PredmetEntity>(predmet, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/pronadjiPremaId/{idPredmeta}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiPredmetPremaId(@PathVariable Integer idPredmeta) {

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Predmet sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		return new ResponseEntity<PredmetEntity>(predmet, HttpStatus.OK);

	}
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	

}
