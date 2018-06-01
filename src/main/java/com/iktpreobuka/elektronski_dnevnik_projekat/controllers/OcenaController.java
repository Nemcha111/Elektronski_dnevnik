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

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OcenaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipOcene;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OcenaRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.PredNastOdeljenjaRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.EmailService;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/ocena")
public class OcenaController {

	@Autowired
	public OcenaRepository ocenaRepo;

	@Autowired
	public UcenikRepository ucenikRepo;

	@Autowired
	public PredNastOdeljenjaRepository predNastOdeljenjaRepo;
	
	@Autowired
	public EmailService emailService;

	@RequestMapping(value = "/ucenik/{idUcenika}/pred-nast-odeljenja/{idPredNastOdeljenja}", method = RequestMethod.POST)
	public ResponseEntity<?> kreirajeOcene(@Valid @RequestBody OcenaEntity ocena, @PathVariable Integer idUcenika,
			@PathVariable Integer idPredNastOdeljenja, BindingResult result) throws Exception {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);

		} else if (predNastOdeljenjaRepo.findById(idPredNastOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OcenaEntity novaOcena = new OcenaEntity();

		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		PredNastOdeljenjaEntity pno = predNastOdeljenjaRepo.findById(idPredNastOdeljenja).get();

		if (ucenik.getOdeljenjeUcenika() != pno.getPredajeOdeljenju()) {
			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja ne predaje ni jedan predmet ovom uceniku."),
					HttpStatus.BAD_REQUEST);
		}

		novaOcena.setOcena(ocena.getOcena());
		novaOcena.setDatumOcene(ocena.getDatumOcene());
		novaOcena.setTipOcene(ETipOcene.REDOVNA);
		novaOcena.setOcenaUcenika(ucenik);
		novaOcena.setOcenioPredmetniNastavnik(pno);

		OcenaEntity ocenaEntity = ocenaRepo.save(novaOcena);
		
		
		emailService.sendTemplateMessage(ocenaEntity);
		return new ResponseEntity<OcenaEntity>(ocenaEntity, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihOcena() {

		List<OcenaEntity> ocene = new ArrayList<>();

		ocene = (List<OcenaEntity>) ocenaRepo.findAll();

		if (ocene.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedana ocena."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OcenaEntity>>(ocenaRepo.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/obrisi/{idOcene}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeocene(@PathVariable Integer idOcene) {

		if (ocenaRepo.findById(idOcene).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ocena sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OcenaEntity ocena = ocenaRepo.findById(idOcene).get();

		ocenaRepo.deleteById(idOcene);
		return new ResponseEntity<OcenaEntity>(ocena, HttpStatus.OK);

	}
	
	
	@RequestMapping (value = "/izmeni/idOcene/{idOcene}", method = RequestMethod.PUT)
	private ResponseEntity<?> izmenaOcene (@Valid@RequestBody OcenaEntity novaOcena, @PathVariable Integer idOcene,  BindingResult result){
		

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		if (ocenaRepo.findById(idOcene).isPresent()==false) {
			
			return new ResponseEntity<RESTError>(new RESTError("Ocena sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
			}
		
		
		OcenaEntity ocena = ocenaRepo.findById(idOcene).get();
		
		ocena.setOcena(novaOcena.getOcena());
		ocena.setDatumOcene(novaOcena.getDatumOcene());
	
		return new ResponseEntity<OcenaEntity>(ocenaRepo.save(ocena), HttpStatus.OK);
		
		
		
	}
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	
	

}
