package com.iktpreobuka.elektronski_dnevnik_projekat.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RazredRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.util.RESTError;

@Service
public class OdeljenjeServiceImpl implements OdeljenjeService {
	
	@Autowired
	public OdeljenjeRepository odeljenjeRepo;
	
	@Autowired
	public RazredRepository razredRepo;
	
	@Override
	public ResponseEntity<?> proveraIKreiranjeOdeljenja(OdeljenjeEntity odeljenje, Integer idRazreda){
		
		List<OdeljenjeEntity> odeljenja = (List<OdeljenjeEntity>) odeljenjeRepo.findAll();

		for (OdeljenjeEntity odeljenjeEntity : odeljenja) {

			if (odeljenjeEntity.getRazred().getIdRazreda() == idRazreda) {
				if (odeljenjeEntity.getImeOdeljenja().equals(odeljenje.getImeOdeljenja()))

					return new ResponseEntity<RESTError>(
							new RESTError("Odeljenje koje pokusavate da kreirte vec postoji."), HttpStatus.BAD_REQUEST);
			}

		}

		if (razredRepo.findById(idRazreda).isPresent() == false) {

			return new ResponseEntity<RESTError>(new RESTError("Razred sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}

		OdeljenjeEntity novoOdeljenje = new OdeljenjeEntity();
		RazredEntity razred = razredRepo.findById(idRazreda).get();

		novoOdeljenje.setImeOdeljenja(odeljenje.getImeOdeljenja());
		novoOdeljenje.setRazred(razredRepo.save(razred));

		odeljenjeRepo.save(novoOdeljenje);

		return new ResponseEntity<OdeljenjeEntity>(odeljenjeRepo.save(novoOdeljenje), HttpStatus.OK);
		
	}

}
