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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RoditeljRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.EncryptionService;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@RestController
@RequestMapping(path = "/API/version1/roditelj")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepo;

	@Autowired
	private EncryptionService encryptionService;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihRoditelja() {

		List<RoditeljEntity> roditelji = new ArrayList<>();

		roditelji = (List<RoditeljEntity>) roditeljRepo.findAll();

		if (roditelji.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan roditelj."),
					HttpStatus.NOT_FOUND);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je pozvao prikazivanje svih roditelja.");
		
		return new ResponseEntity<Iterable<RoditeljEntity>>(roditeljRepo.findAll(), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodajNovogRoditelja(@Valid @RequestBody RoditeljEntity roditelj, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		List<RoditeljEntity> roditelji = (List<RoditeljEntity>) roditeljRepo.findAll();

		for (RoditeljEntity roditeljEntity : roditelji) {
			if (roditelj.getKorisnickoImeRoditelja().equals(roditeljEntity.getKorisnickoImeRoditelja())) {
				return new ResponseEntity<RESTError>(new RESTError("Ovo korisnicko ime vec postoji."),
						HttpStatus.NOT_FOUND);
			}
		}

		RoditeljEntity noviRoditelj = new RoditeljEntity();

		noviRoditelj.setImeRoditelja(roditelj.getImeRoditelja());
		noviRoditelj.setPrezimeRoditelja(roditelj.getPrezimeRoditelja());
		noviRoditelj.setKorisnickoImeRoditelja(roditelj.getKorisnickoImeRoditelja());

		noviRoditelj.setSifraRoditelja(encryptionService.enkriptor(roditelj.getSifraRoditelja()));
		noviRoditelj.setEmailRoditelja(roditelj.getEmailRoditelja());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je kreirao novog roditelja sa korisnickim imenom "
				+ noviRoditelj.getKorisnickoImeRoditelja());
		
		return new ResponseEntity<RoditeljEntity>(roditeljRepo.save(noviRoditelj), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je izmenio roditelja sa korisnickim imenom "
				+ noviRoditelj.getKorisnickoImeRoditelja());

		return new ResponseEntity<RoditeljEntity>(roditeljRepo.save(roditelj), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.warn("Korisnik " + currentPrincipalName + " je obrisao roditelja sa korisnickim imenom "
				+ roditelj.getKorisnickoImeRoditelja());
		
		return new ResponseEntity<RoditeljEntity>(roditelj, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/pronadjiPremaId/{idRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiRoditeljaPremaId(@PathVariable Integer idRoditelja) {

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je pozvao prikazivanje roditelja po ID sa korisnickim imenom "
				+ roditelj.getKorisnickoImeRoditelja());
		
		return new ResponseEntity<RoditeljEntity>(roditelj, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/ime/{imeRoditelja}/prezime/{prezimeRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiRoditeljaPremaImenuIPrezimenu(@PathVariable String imeRoditelja, @PathVariable String prezimeRoditelja) {

		
		List<RoditeljEntity> roditelji = roditeljRepo.findByImeRoditelja(imeRoditelja);
		
		
		if ((roditelji.size() == 0)) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim imenom postoji."), HttpStatus.NOT_FOUND);
		} 
		

		for (RoditeljEntity roditeljEntity : roditelji) {
			if (!roditeljEntity.getPrezimeRoditelja().equalsIgnoreCase(prezimeRoditelja)) {
				return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim imenom i prezimenom ne postoji."),	HttpStatus.NOT_FOUND);
			}

		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je pozvao prikazivanje roditelja sa imenom" + imeRoditelja +" i prezimenom " + prezimeRoditelja);

		return new ResponseEntity<Iterable<RoditeljEntity>>(roditelji, HttpStatus.OK);

	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
