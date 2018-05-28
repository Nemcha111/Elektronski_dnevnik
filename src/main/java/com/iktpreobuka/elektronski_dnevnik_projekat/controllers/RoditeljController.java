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
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RoditeljRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/roditelj")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepo;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihRoditelja() {

		List<RoditeljEntity> roditelji = new ArrayList<>();

		roditelji = (List<RoditeljEntity>) roditeljRepo.findAll();

		if (roditelji.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan roditelj."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<RoditeljEntity>>(roditeljRepo.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogRoditelja(@Valid @RequestBody RoditeljEntity roditelj, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		RoditeljEntity noviRoditelj = new RoditeljEntity();

		noviRoditelj.setImeRoditelja(roditelj.getImeRoditelja());
		noviRoditelj.setPrezimeRoditelja(roditelj.getPrezimeRoditelja());
		noviRoditelj.setKorisnickoImeRoditelja(roditelj.getKorisnickoImeRoditelja());
		noviRoditelj.setSifraRoditelja(roditelj.getSifraRoditelja());
		noviRoditelj.setEmailRoditelja(roditelj.getEmailRoditelja());

		return new ResponseEntity<RoditeljEntity>(roditeljRepo.save(noviRoditelj), HttpStatus.OK);
	}

	

	@RequestMapping(value = "/izmena/{idRoditelja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaRoditelja(@Valid @RequestBody RoditeljEntity noviRoditelj,
			@PathVariable Integer idRoditelja, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj kojeg zelite da izmenite ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();

		roditelj.setImeRoditelja(noviRoditelj.getImeRoditelja());
		roditelj.setPrezimeRoditelja(noviRoditelj.getPrezimeRoditelja());
		roditelj.setKorisnickoImeRoditelja(noviRoditelj.getKorisnickoImeRoditelja());
		roditelj.setEmailRoditelja(noviRoditelj.getEmailRoditelja());

		return new ResponseEntity<RoditeljEntity>(roditeljRepo.save(roditelj), HttpStatus.OK);
	}

	@RequestMapping(value = "/obrisi/{idRoditelja}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeRoditelja(@PathVariable Integer idRoditelja) {

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		if (roditelj.getDeca().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ovaj roditelj ima dodeljeno dete/decu te ga ne mozete obrisati."),
					HttpStatus.NOT_FOUND);
		}

		roditeljRepo.deleteById(idRoditelja);
		return new ResponseEntity<RoditeljEntity>(roditelj, HttpStatus.OK);

	}

	@RequestMapping(value = "/pronadjiPremaId/{idRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiRoditeljaPremaId(@PathVariable Integer idRoditelja) {

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		return new ResponseEntity<RoditeljEntity>(roditelj, HttpStatus.OK);

	}
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
