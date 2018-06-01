package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RazredRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@Service
public class RazredServiceImpl implements RazredService {

	@Autowired
	private RazredRepository razredRepo;

	@Override
	public ResponseEntity<?> proveraIKreiranjeRazreda(RazredEntity razred) {

		List<RazredEntity> razredi = (List<RazredEntity>) razredRepo.findAll();

		for (RazredEntity razredEntity : razredi) {

			//Integer test1 = razredEntity.getPocetakSkolskeGodine();
			//Integer test2 = razred.getPocetakSkolskeGodine();

			//if (razredEntity.getPocetakSkolskeGodine().intValue() ==razred.getPocetakSkolskeGodine().intValue()) {// --NE RADI ZASTO
			 if (razredEntity.getPocetakSkolskeGodine().equals( razred.getPocetakSkolskeGodine())) {
			//if (test1 == test2) {
				return new ResponseEntity<RESTError>(new RESTError("Razredi za tu skolsku godinu su vec kreirani."),
						HttpStatus.BAD_REQUEST);
			}

		}

		for (int i = 0; i < 8; i++) {
			RazredEntity noviRazred = new RazredEntity();
			noviRazred.setRazred(i + 1);
			noviRazred.setSkolskaGodinaRazreda(String.valueOf(razred.getPocetakSkolskeGodine()) + "/"
					+ String.valueOf(razred.getPocetakSkolskeGodine() + 1));
			noviRazred.setPocetakSkolskeGodine(razred.getPocetakSkolskeGodine());
			razredRepo.save(noviRazred);

		}

		return new ResponseEntity<Iterable<RazredEntity>>(razredRepo.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> proveraIKreiranjePojedniacnogRazreda(RazredEntity razred) {

		List<RazredEntity> razredi = (List<RazredEntity>) razredRepo.findAll();

		for (RazredEntity razredEntity : razredi) {

			if (razredEntity.getRazred().equals(razred.getRazred())) {
				if (razredEntity.getSkolskaGodinaRazreda().equals(razred.getSkolskaGodinaRazreda()))

					return new ResponseEntity<RESTError>(
							new RESTError("Razred koji pokusavate da kreirte vec postoji."), HttpStatus.BAD_REQUEST);
			}

		}

		RazredEntity noviRazred = new RazredEntity();
		noviRazred.setRazred(razred.getRazred());
		noviRazred.setSkolskaGodinaRazreda(razred.getSkolskaGodinaRazreda());

		razredRepo.save(noviRazred);

		return new ResponseEntity<RazredEntity>(razredRepo.save(noviRazred), HttpStatus.OK);

	}
}
