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

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RoditeljRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(value = "/API/version1/ucenik")
public class UcenikController {

	@Autowired
	private UcenikRepository ucenikRepo;

	@Autowired
	private RoditeljRepository roditeljRepo;
	
	@Autowired
	private OdeljenjeRepository odeljenjeRepo;

	@RequestMapping(value = "roditelj/{idRoditelja}/odeljenje/{idOdeljenja}", method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogUcenika(@Valid @RequestBody UcenikEntity ucenik,
			@PathVariable Integer idRoditelja, @PathVariable Integer idOdeljenja, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		List<UcenikEntity> ucenici = (List<UcenikEntity>) ucenikRepo.findAll();

		for (UcenikEntity ucenikEntity : ucenici) {
			if (ucenik.getKorisnickoImeUcenika().equals(ucenikEntity.getKorisnickoImeUcenika())) {
				return new ResponseEntity<RESTError>(new RESTError("Ovo korisnicko ime vec postoji."),
						HttpStatus.NOT_FOUND);
			}
		}

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Odeljenje sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		UcenikEntity noviUcenik = new UcenikEntity();

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();

		noviUcenik.setImeUcenika(ucenik.getImeUcenika());
		noviUcenik.setPrezimeUcenika(ucenik.getPrezimeUcenika());
		noviUcenik.setKorisnickoImeUcenika(ucenik.getKorisnickoImeUcenika());
		noviUcenik.setSifraUcenika(ucenik.getSifraUcenika());
		noviUcenik.setRoditelj(ucenik.getRoditelj());
		noviUcenik.setOdeljenjeUcenika(ucenik.getOdeljenjeUcenika());

		noviUcenik.setRoditelj(roditelj);
		noviUcenik.setOdeljenjeUcenika(odeljenje);

		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(noviUcenik), HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihUcenika() {

		List<UcenikEntity> ucenici = new ArrayList<>();

		ucenici = (List<UcenikEntity>) ucenikRepo.findAll();

		if (ucenici.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan ucenik."), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<UcenikEntity>>(ucenikRepo.findAll(), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/pronadjiPremaId/{idUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiUcenikaPoId(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		return new ResponseEntity<UcenikEntity>(ucenik, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/obrisi/{idUcenika}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeUcenika(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik kojeg zelite da obrisete ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		if (ucenik.getOcene().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ovaj ucenik ima dodeljenu barem jednu ocenu te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}

		ucenikRepo.deleteById(idUcenika);
		return new ResponseEntity<UcenikEntity>(ucenik, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/izmena/{idUcenika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaUcenika(@Valid @RequestBody UcenikEntity noviUcenik,
			@PathVariable Integer idUcenika, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik kojeg zelite da izmenite ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();

		ucenik.setImeUcenika(noviUcenik.getImeUcenika());
		ucenik.setPrezimeUcenika(noviUcenik.getPrezimeUcenika());
		ucenik.setKorisnickoImeUcenika(noviUcenik.getKorisnickoImeUcenika());
	

		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/izmena/{idUcenika}/roditelj/{idRoditelja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaRoditeljaUceniku(@PathVariable Integer idUcenika,
			@PathVariable Integer idRoditelja) {
		
		
		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		
		ucenik.setRoditelj(roditelj);
		
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);
		
		
	}
	
	
	@RequestMapping(value = "/izmena/{idUcenika}/odeljenje/{idOdeljenja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaOdeljenjaUceniku(@PathVariable Integer idUcenika,
			@PathVariable Integer idOdeljenja) {
		
		
		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Odeljenje sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		
		ucenik.setOdeljenjeUcenika(odeljenje);
		
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);
		
		
	}
	
	
	
	
	@RequestMapping(value = "/pronadjiPremaRoditelju/{idRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiUcenikaPoRoditelju(@PathVariable Integer idRoditelja) {

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		
		List<UcenikEntity> ucenici = (List<UcenikEntity>) ucenikRepo.findByRoditelj(roditelj);
		
		if (roditelj.getDeca().size() == 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ovaj roditelj nema ni jedno dete u skoli."),
					HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<Iterable<UcenikEntity>>(ucenici, HttpStatus.OK);

	}

}
