package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OcenaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipOcene;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OcenaRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.PredNastOdeljenjaRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RazredRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.EmailService;
import com.iktpreobuka.elektronski_dnevnik_projekat.services.OcenaService;
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
	public RazredRepository razredRepo;

	@Autowired
	public OcenaService ocenaService;

	@Autowired
	public EmailService emailService;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Secured("ROLE_NASTAVNIK")
	@PreAuthorize("#username == authentication.principal.username")
	@RequestMapping(value = "/ucenik/{idUcenika}/pred-nast-odeljenja/{idPredNastOdeljenja}/korisnicko-ime-nastavnika/{username}", method = RequestMethod.POST)
	public ResponseEntity<?> kreirajeOcene(@Valid @RequestBody OcenaEntity ocena, @PathVariable Integer idUcenika,
			@PathVariable Integer idPredNastOdeljenja, @PathVariable String username, BindingResult result) throws Exception {

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
		
		if(!pno.getNastavnik().getKorisnickoImeNastavnika().equals(username)) {
			return new ResponseEntity<RESTError>(
					new RESTError("Nije vam dozvoljeno da dodelite navedenom uceniku ocenu iz navedenog predmeta. Proverite da li ste tacno uneli predmet i ucenika"),
					HttpStatus.BAD_REQUEST);
			
		}

		if (ucenik.getOdeljenjeUcenika() != pno.getPredajeOdeljenju()) {
			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja ne predaje ni jedan predmet ovom uceniku."),
					HttpStatus.BAD_REQUEST);
		}

		novaOcena.setOcena(ocena.getOcena());
		if(ocena.getOcena()<1 || ocena.getOcena()>5) {
			return new ResponseEntity<RESTError>(
					new RESTError("Ocena mora biti uneta kao jednocifreni broj izmedju 1 i 5."),
					HttpStatus.BAD_REQUEST);
		}
		novaOcena.setDatumOcene(new Date());
		novaOcena.setTipOcene(ETipOcene.REDOVNA);
		novaOcena.setOcenaUcenika(ucenik);
		novaOcena.setOcenioPredmetniNastavnik(pno);
		novaOcena.setPredmet(pno.getPredajePredmet().getImePredmeta());
		novaOcena.setNastavnik(pno.getNastavnik().getImeNastavnika() + " " + pno.getNastavnik().getPrezimeNastavnika());
		novaOcena.setPunoImeUcenika(ucenik.getImeUcenika() + " " + ucenik.getPrezimeUcenika());
		novaOcena.setIdUcenika(idUcenika);
		novaOcena.setSkolskaGodinaOcene(ucenik.getOdeljenjeUcenika().getRazredOdeljenja().getSkolskaGodinaRazreda());

		OcenaEntity ocenaEntity = ocenaRepo.save(novaOcena);

		if (novaOcena.getOcenaUcenika().getRoditelj().getEmailRoditelja().equals("nemanja90tem@yahoo.com")) {
			emailService.sendTemplateMessage(ocenaEntity);
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Nastavnik " + currentPrincipalName + " je dao ocenu " + ocena.getOcena() + " uceniku "
				+ ucenik.getImeUcenika() + ucenik.getPrezimeUcenika());

		return new ResponseEntity<OcenaEntity>(ocenaEntity, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihOcena() {

		List<OcenaEntity> ocene = new ArrayList<>();

		ocene = (List<OcenaEntity>) ocenaRepo.findAll();

		if (ocene.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjena ni jedana ocena."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OcenaEntity>>(ocenaRepo.findAll(), HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_RODITELJ" })
	@RequestMapping(value = "/prikaz-ocena-ucenika/{idUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihOcenaUcenika(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		List<OcenaEntity> ocene = (List<OcenaEntity>) ocenaRepo.findAll();

		List<OcenaEntity> oceneUcenika = new ArrayList<>();

		for (OcenaEntity ocenaEntity : ocene) {
			if (ocenaEntity.getIdUcenika() == idUcenika) {
				oceneUcenika.add(ocenaEntity);

			}
		}

		if (oceneUcenika.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Za trazenog ucenika nije pronadjena ni jedana ocena."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OcenaEntity>>(oceneUcenika, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/ucenik-prikaz-ocena/{username}", method = RequestMethod.GET)
	@PreAuthorize("#username == authentication.principal.username")
	public ResponseEntity<?> UcenikpregledSvihOcena(@PathVariable String username) {
		
		 UcenikEntity ucenik = ucenikRepo.findByKorisnickoImeUcenika(username);
		 

		if (ucenikRepo.findById(ucenik.getIdUcenika()).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa prosledjenim korisnickim imenom ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		List<OcenaEntity> ocene = (List<OcenaEntity>) ocenaRepo.findAll();

		List<OcenaEntity> oceneUcenika = new ArrayList<>();

		for (OcenaEntity ocenaEntity : ocene) {
			if (ocenaEntity.getIdUcenika() == ucenik.getIdUcenika()) {
				oceneUcenika.add(ocenaEntity);

			}
		}

		if (oceneUcenika.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Za trazenog ucenika nije pronadjena ni jedana ocena."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OcenaEntity>>(oceneUcenika, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/obrisi/{idOcene}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeOcene(@PathVariable Integer idOcene) {

		if (ocenaRepo.findById(idOcene).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ocena sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OcenaEntity ocena = ocenaRepo.findById(idOcene).get();
		
		ocenaRepo.deleteById(idOcene);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.warn("Administrator " + currentPrincipalName + " je obrisao ocenu sa ID-jem " + ocena.getIdOcene());

		
		return new ResponseEntity<OcenaEntity>(ocena, HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/izmeni/idOcene/{idOcene}", method = RequestMethod.PUT)
	private ResponseEntity<?> izmenaOcene(@Valid @RequestBody OcenaEntity novaOcena, @PathVariable Integer idOcene,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (ocenaRepo.findById(idOcene).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Ocena sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OcenaEntity ocena = ocenaRepo.findById(idOcene).get();

		ocena.setOcena(novaOcena.getOcena());
		ocena.setDatumOcene(new Date());
		// ocena.setSkolskaGodinaOcene(novaOcena.getSkolskaGodinaOcene());
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.info("Administrator " + currentPrincipalName + " je izmenio ocenu sa ID-jem " + ocena.getIdOcene());
		

		return new ResponseEntity<OcenaEntity>(ocenaRepo.save(ocena), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/zakljucivanje-ocena-polugodiste", method = RequestMethod.POST)
	public ResponseEntity<?> zakljucivanjeOcenaPolugodista(@RequestParam String skolskaGodina) {

		List<RazredEntity> razredi = (List<RazredEntity>) razredRepo.findBySkolskaGodinaRazreda(skolskaGodina);

		for (RazredEntity razredEntity : razredi) {

			if (razredEntity.getSkolskaGodinaRazreda().equals(skolskaGodina))

				return ocenaService.zakljucivanjeOcenaPolugodisete(skolskaGodina);
		}

		return new ResponseEntity<RESTError>(
				new RESTError("Skolska godina za koju pokusavante da zakljucite ocene ne postoji."),
				HttpStatus.BAD_REQUEST);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/zakljucivanje-ocena-zavrsnih", method = RequestMethod.POST)
	public ResponseEntity<?> zakljucivanjeOcenaZavrsnih(@RequestParam String skolskaGodina) {

		List<RazredEntity> razredi = (List<RazredEntity>) razredRepo.findBySkolskaGodinaRazreda(skolskaGodina);

		for (RazredEntity razredEntity : razredi) {

			if (razredEntity.getSkolskaGodinaRazreda().equals(skolskaGodina)) {

				return ocenaService.zakljucivanjeOcenaZavrsnih(skolskaGodina);
			}

		}
		return new ResponseEntity<RESTError>(
				new RESTError("Skolska godina za koju pokusavante da zakljucite ocene ne postoji."),
				HttpStatus.BAD_REQUEST);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/brisanje-zakljucnih-ocena-polugodista", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeOcenaPolugodista(@RequestParam String skolskaGodina) {

		List<OcenaEntity> sveOceneSkolskeGodine = (List<OcenaEntity>) ocenaRepo.findBySkolskaGodinaOcene(skolskaGodina);

		List<OcenaEntity> ocenePolugodista = new ArrayList<>();

		for (OcenaEntity ocena : sveOceneSkolskeGodine) {

			if (ocena.getTipOcene().equals(ETipOcene.POLUGODISTE)) {

				ocenePolugodista.add(ocena);
			}
		}

		if (ocenePolugodista.size() <= 0) {

			return new ResponseEntity<RESTError>(new RESTError(
					"Ne postoji ni jedna zakljucna ocena polugodista za unetu skolsku godinu. Proverite da li ste ispravno uneli skolsku godinu"),
					HttpStatus.BAD_REQUEST);
		}
		

		for (OcenaEntity ocenaEntity : ocenePolugodista) {

			
			ocenaRepo.delete(ocenaEntity);
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.warn("Administrator " + currentPrincipalName + " je obrisao ocene za polugodiste skolske godine " + skolskaGodina);
		
		
		return new ResponseEntity<Iterable<OcenaEntity>>(ocenePolugodista, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/brisanje-zakljucnih-ocena", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisanjeOcenaZavrsnih(@RequestParam String skolskaGodina) {

		List<OcenaEntity> sveOceneSkolskeGodine = (List<OcenaEntity>) ocenaRepo.findBySkolskaGodinaOcene(skolskaGodina);

		List<OcenaEntity> oceneZakljucne = new ArrayList<>();

		for (OcenaEntity ocena : sveOceneSkolskeGodine) {

			if (ocena.getTipOcene().equals(ETipOcene.ZAKLJUCNA)) {

				oceneZakljucne.add(ocena);
			}
		}

		if (oceneZakljucne.size() <= 0) {

			return new ResponseEntity<RESTError>(new RESTError(
					"Ne postoji ni jedna zakljucna ocena polugodista za unetu skolsku godinu. Proverite da li ste ispravno uneli skolsku godinu."),
					HttpStatus.BAD_REQUEST);
		}
		
		
		for (OcenaEntity ocenaEntity : oceneZakljucne) {

			ocenaRepo.delete(ocenaEntity);
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		logger.warn("Administrator " + currentPrincipalName + " je obrisao zakuljucne ocene skolske godine " + skolskaGodina);
		
		return new ResponseEntity<Iterable<OcenaEntity>>(oceneZakljucne, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/prikaz-zakljucnih-ocena-polugodista", method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihOcenaPolugodista(@RequestParam String skolskaGodinaOcene) {

		logger.info("Pregled svih zakljucnih ocena polugodista");

		List<OcenaEntity> sveOceneSkolskeGodine = (List<OcenaEntity>) ocenaRepo
				.findBySkolskaGodinaOcene(skolskaGodinaOcene);

		List<OcenaEntity> ocenePolugodista = new ArrayList<>();

		for (OcenaEntity ocena : sveOceneSkolskeGodine) {

			if (ocena.getTipOcene().equals(ETipOcene.POLUGODISTE)) {

				ocenePolugodista.add(ocena);
			}
		}

		if (ocenePolugodista.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError(
					"Ne postoji ni jedna zakljucna ocena polugodista za unetu skolsku godinu. Proverite da li ste ispravno uneli skolsku godinu."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OcenaEntity>>(ocenePolugodista, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/prikaz-zakljucnih-ocena-zavrsnih", method = RequestMethod.GET)
	public ResponseEntity<?> pregledSvihOcenaZavrsnih(@RequestParam String skolskaGodinaOcene) {

		logger.info("Pregled svih zakljucnih ocena");

		List<OcenaEntity> sveOceneSkolskeGodine = (List<OcenaEntity>) ocenaRepo
				.findBySkolskaGodinaOcene(skolskaGodinaOcene);

		List<OcenaEntity> ocenePolugodista = new ArrayList<>();

		for (OcenaEntity ocena : sveOceneSkolskeGodine) {

			if (ocena.getTipOcene().equals(ETipOcene.ZAKLJUCNA)) {

				ocenePolugodista.add(ocena);
			}
		}

		if (ocenePolugodista.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError(
					"Ne postoji ni jedna zakljucna ocena polugodista za unetu skolsku godinu. Proverite da li ste ispravno uneli skolsku godinu."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Iterable<OcenaEntity>>(ocenePolugodista, HttpStatus.OK);
	}


	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}


//logger.debug("This is a debug message");
//logger.info("This is an info message");
//logger.warn("This is a warn message");
//logger.error("This is an error message");

