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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.iktpreobuka.elektronski_dnevnik_projekat.services.EncryptionService;
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

	@Autowired
	private EncryptionService encryptionService;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
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
		//noviUcenik.setSifraUcenika(ucenik.getSifraUcenika());
		noviUcenik.setSifraUcenika(encryptionService.enkriptor(ucenik.getSifraUcenika()));
		
		noviUcenik.setRoditelj(roditelj);
		noviUcenik.setOdeljenjeUcenika(odeljenje);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je kreirao novog ucenika sa korisnickim imenom "
				+ noviUcenik.getKorisnickoImeUcenika());

		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(noviUcenik), HttpStatus.OK);
	}

	

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihUcenika() {

		List<UcenikEntity> ucenici = new ArrayList<>();

		ucenici = (List<UcenikEntity>) ucenikRepo.findAll();

		if (ucenici.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan ucenik."),
					HttpStatus.NOT_FOUND);
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je pozvao prikazivanje svih ucenika");

		return new ResponseEntity<Iterable<UcenikEntity>>(ucenikRepo.findAll(), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/pronadjiPremaId/{idUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiUcenikaPoId(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je pozvao prikazivanje ucenika sa korisnickim imenom "
				+ ucenik.getKorisnickoImeUcenika());
		
		return new ResponseEntity<UcenikEntity>(ucenik, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.warn("Korisnik " + currentPrincipalName + " je obrisao ucenika sa korisnickim imenom "
				+ ucenik.getKorisnickoImeUcenika());
		
		return new ResponseEntity<UcenikEntity>(ucenik, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/izmena/{idUcenika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenaUcenika(@Valid @RequestBody UcenikEntity noviUcenik, @PathVariable Integer idUcenika,
			BindingResult result) {

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

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je izmenio ucenika sa korisnickim imenom "
				+ ucenik.getKorisnickoImeUcenika());
		
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je izmenio roditelja ucenika sa korisnickim imenom "
				+ ucenik.getKorisnickoImeUcenika());

		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
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

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je promenio odeljenje ucenika sa korisnickim imenom "
				+ ucenik.getKorisnickoImeUcenika());
		
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);

	}

	
	@RequestMapping(value = "/pronadjiPremaRoditelju/{username}", method = RequestMethod.GET)
	@PreAuthorize("#username == authentication.principal.username")
	public ResponseEntity<?> pronadjiUcenikaPoRoditelju(@PathVariable String username) {

		logger.info("Korisnik " + username + "je zatrazio prikazivanje svoje dece");

		if (roditeljRepo.findByKorisnickoImeRoditelja(username) == null) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findByKorisnickoImeRoditelja(username);

		if (roditelj.getDeca().size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ovaj roditelj nema ni jedno dete u skoli."),
					HttpStatus.NOT_FOUND);
		}

		List<UcenikEntity> ucenici = (List<UcenikEntity>) ucenikRepo.findByRoditelj(roditelj);

		
		return new ResponseEntity<Iterable<UcenikEntity>>(ucenici, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/pronadjiPremaRoditelju/{korisnickoImeRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiUcenikaPoRoditeljuAdmin(@PathVariable String korisnickoImeRoditelja) {

		// OVO POKAZUJE KOJI KORISNIK SAlJE ZAHTEV
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je zatrazio prikazivanje dece korisnika "
				+ korisnickoImeRoditelja);

		if (roditeljRepo.findByKorisnickoImeRoditelja(korisnickoImeRoditelja) == null) {

			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		RoditeljEntity roditelj = roditeljRepo.findByKorisnickoImeRoditelja(korisnickoImeRoditelja);

		if (roditelj.getDeca().size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ovaj roditelj nema ni jedno dete u skoli."),
					HttpStatus.NOT_FOUND);
		}

		List<UcenikEntity> ucenici = (List<UcenikEntity>) ucenikRepo.findByRoditelj(roditelj);

		return new ResponseEntity<Iterable<UcenikEntity>>(ucenici, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/ime/{imeUcenika}/prezime/{prezimeUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiUcenikaPremaImenuIPrezimenu(@PathVariable String imeUcenika,
			@PathVariable String prezimeUcenika) {

		List<UcenikEntity> ucenici = ucenikRepo.findByImeUcenika(imeUcenika);

		if ((ucenici.size() == 0)) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim imenom ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		for (UcenikEntity ucenikEntity : ucenici) {
			if (!ucenikEntity.getPrezimeUcenika().equalsIgnoreCase(prezimeUcenika)) {
				return new ResponseEntity<RESTError>(
						new RESTError("Ucenik sa prosledjenim imenom i prezimenom ne postoji."), HttpStatus.NOT_FOUND);
			}

		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Korisnik " + currentPrincipalName + " je pozvao prikazivanje ucenika sa imenom "
				+ imeUcenika + " i prezimenom " + prezimeUcenika);
		
		return new ResponseEntity<Iterable<UcenikEntity>>(ucenici, HttpStatus.OK);

	}
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
