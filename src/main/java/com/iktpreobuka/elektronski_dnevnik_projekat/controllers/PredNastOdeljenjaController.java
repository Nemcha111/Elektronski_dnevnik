package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.NastavnikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredmetEntity;

import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.NastavnikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.PredNastOdeljenjaRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.PredmetRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/predmetni-nastavnik-odeljenja")
public class PredNastOdeljenjaController {

	@Autowired
	PredNastOdeljenjaRepository predNastOdeljenjaRepo;

	@Autowired
	public PredmetRepository predmetRepo;

	@Autowired
	public NastavnikRepository nastavnikRepo;

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	@RequestMapping(path = "/predmet/{idPredmeta}/nastavnik/{idNastavnika}/odeljenje/{idOdeljenja}", method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogPredNastOdeljenja(@PathVariable Integer idPredmeta,
			@PathVariable Integer idNastavnika, @PathVariable Integer idOdeljenja) {

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Predmet sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		} else if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Nastavnik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		} else if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Odeljenje sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredNastOdeljenjaEntity noviPredNastOdeljenja = new PredNastOdeljenjaEntity();

		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();

		noviPredNastOdeljenja.setPredajePredmet(predmet);
		noviPredNastOdeljenja.setNastavnik(nastavnik);
		noviPredNastOdeljenja.setPredajeOdeljenju(odeljenje);

		return new ResponseEntity<PredNastOdeljenjaEntity>(predNastOdeljenjaRepo.save(noviPredNastOdeljenja),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/obrisi/{idPredNastOdeljenja}", method = RequestMethod.DELETE)
	public ResponseEntity<?> obrisiPredNastOdeljenja(@PathVariable Integer idPredNastOdeljenja) {

		if (predNastOdeljenjaRepo.findById(idPredNastOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredNastOdeljenjaEntity pno = predNastOdeljenjaRepo.findById(idPredNastOdeljenja).get();
		if (pno.getOcene().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError(
							"Ovaj predmetni nastavnik odeljenja je vec dodeljivao ocene te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}
		predNastOdeljenjaRepo.deleteById(idPredNastOdeljenja);
		return new ResponseEntity<PredNastOdeljenjaEntity>(pno, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihPredNastOdeljenja() {

		List<PredNastOdeljenjaEntity> pno = new ArrayList<>();

		pno = (List<PredNastOdeljenjaEntity>) predNastOdeljenjaRepo.findAll();

		if (pno.size() == 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Nije pronadjen ni jedan predmetni nastavnik odeljenja."), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<PredNastOdeljenjaEntity>>(predNastOdeljenjaRepo.findAll(), HttpStatus.OK);

	}

	@RequestMapping(value = "/pronadjiPremaId/{idPredNastOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiNastPredOdeljenjaPremaId(@PathVariable Integer idPredNastOdeljenja) {

		if (predNastOdeljenjaRepo.findById(idPredNastOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredNastOdeljenjaEntity pno = predNastOdeljenjaRepo.findById(idPredNastOdeljenja).get();
		return new ResponseEntity<PredNastOdeljenjaEntity>(pno, HttpStatus.OK);

	}

	@RequestMapping(value = "/izmena/{idPredNastOdeljenja}/predmet/{idPredmeta}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaPredmetaPredNastOdeljenja(@PathVariable Integer idPredNastOdeljenja,
			@PathVariable Integer idPredmeta) {

		if (predNastOdeljenjaRepo.findById(idPredNastOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Predmet sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredNastOdeljenjaEntity pno = predNastOdeljenjaRepo.findById(idPredNastOdeljenja).get();
		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();

		pno.setPredajePredmet(predmet);

		return new ResponseEntity<PredNastOdeljenjaEntity>(predNastOdeljenjaRepo.save(pno), HttpStatus.OK);

	}

	@RequestMapping(value = "/izmena/{idPredNastOdeljenja}/nastavnik/{idNastavnika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaNastavnikaPredNastOdeljenja(@PathVariable Integer idPredNastOdeljenja,
			@PathVariable Integer idNastavnika) {

		if (predNastOdeljenjaRepo.findById(idPredNastOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Nastavnik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredNastOdeljenjaEntity pno = predNastOdeljenjaRepo.findById(idPredNastOdeljenja).get();
		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();

		pno.setNastavnik(nastavnik);

		return new ResponseEntity<PredNastOdeljenjaEntity>(predNastOdeljenjaRepo.save(pno), HttpStatus.OK);

	}

	@RequestMapping(value = "/izmena/{idPredNastOdeljenja}/odeljenje/{idOdeljenja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaOdeljenjaPredNastOdeljenja(@PathVariable Integer idPredNastOdeljenja,
			@PathVariable Integer idOdeljenja) {

		if (predNastOdeljenjaRepo.findById(idPredNastOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Odeljenje sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		PredNastOdeljenjaEntity pno = predNastOdeljenjaRepo.findById(idPredNastOdeljenja).get();
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();

		pno.setPredajeOdeljenju(odeljenje);

		return new ResponseEntity<PredNastOdeljenjaEntity>(predNastOdeljenjaRepo.save(pno), HttpStatus.OK);

	}

}
