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

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;
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

	@RequestMapping(value = "roditelj/{idRoditelja}", method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogUcenika(@Valid @RequestBody UcenikEntity ucenik,
			@Valid @PathVariable Integer idRoditelja, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		UcenikEntity noviUcenik = new UcenikEntity();

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();

		noviUcenik.setImeUcenika(ucenik.getImeUcenika());
		noviUcenik.setPrezimeUcenika(ucenik.getPrezimeUcenika());
		noviUcenik.setKorisnickoImeUcenika(ucenik.getKorisnickoImeUcenika());
		noviUcenik.setSifraUcenika(ucenik.getSifraUcenika());
		noviUcenik.setRoditelj(ucenik.getRoditelj());
		noviUcenik.setOdeljenjeUcenika(ucenik.getOdeljenjeUcenika());

		noviUcenik.setRoditelj(roditeljRepo.save(roditelj));

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

}
