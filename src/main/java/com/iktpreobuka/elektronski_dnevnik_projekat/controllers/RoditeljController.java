package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RoditeljRepository;

@RestController
@RequestMapping(path = "/API/version1/roditelj")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepo;

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

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
