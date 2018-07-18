package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

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

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.AdminEntity;

import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.AdminRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.EncryptionService;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/admin")
public class AdminController {

	@Autowired
	public AdminRepository adminRepo;

	@Autowired
	public UcenikRepository ucenikRepo;
	
	@Autowired
	private EncryptionService encryptionService;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	//@Secured("ROLE_ADMIN") 
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogAdmina(@Valid @RequestBody AdminEntity admin, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);

		}
		List<AdminEntity> admini = (List<AdminEntity>) adminRepo.findAll();

		for (AdminEntity adminEntity : admini) {
			if (admin.getKorisnickoImeAdmina().equals(adminEntity.getKorisnickoImeAdmina())) {
				return new ResponseEntity<RESTError>(new RESTError("Ovo korisnicko ime vec postoji."),
						HttpStatus.NOT_FOUND);
			}
		}

		AdminEntity noviAdmin = new AdminEntity();

		noviAdmin.setKorisnickoImeAdmina(admin.getKorisnickoImeAdmina());
		noviAdmin.setSifraAdmina(admin.getSifraAdmina());
		//noviAdmin.setSifraAdmina(encryptionService.enkriptor(admin.getSifraAdmina()));

		return new ResponseEntity<AdminEntity>(adminRepo.save(noviAdmin), HttpStatus.OK);
	}

	//@Secured("ROLE_ADMIN") 
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikaziSveAdmine() {

		return new ResponseEntity<Iterable<AdminEntity>>(adminRepo.findAll(), HttpStatus.OK);

	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
