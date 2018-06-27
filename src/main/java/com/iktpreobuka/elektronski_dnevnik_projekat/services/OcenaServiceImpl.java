package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OcenaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipOcene;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.OcenaRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;

@Service
public class OcenaServiceImpl implements OcenaService {

	// @PersistenceContext
	// EntityManager em;

	// select ocena, skolska_godina_razreda
	// from ocene o inner join ucenici u on o.id_ucenika=u.id_ucenika
	// inner join odeljenja od on u.odeljenje_ucenika=od.id_odeljenja
	// inner join razredi r on od.razred_odeljenja=r.id_razreda

	@Autowired
	public OcenaRepository ocenaRepo;

	@Autowired
	public UcenikRepository ucenikRepo;

	@Override
	public ResponseEntity<?> zakljucivanjeOcenaPolugodisete(String skolskaGodinaOcene) {

		List<UcenikEntity> sviUcenici = (List<UcenikEntity>) ucenikRepo.findAll();

		Double prosecnaOcena = 0.;
		Integer zakljucnaOcena = 0;
		Integer brojOcena = 0;
		Integer zbirSvihOcena = 0;

		List<OcenaEntity> sveZakljucneOcenePolugodista = new ArrayList<>();

		for (UcenikEntity ucenikEntity : sviUcenici) {

			List<PredNastOdeljenjaEntity> predmeti = new ArrayList<>();

			predmeti = ucenikEntity.getOdeljenjeUcenika().getPredmetniNastavniciOdeljenja();

			for (PredNastOdeljenjaEntity pno : predmeti) {

				List<OcenaEntity> ocenePredmeta = pno.getOcene();

				zbirSvihOcena = 0;
				brojOcena = 0;

				for (OcenaEntity ocenaEntity : ocenePredmeta) {

					if (ocenaEntity.getSkolskaGodinaOcene().equals(skolskaGodinaOcene)) {

						brojOcena++;

						zbirSvihOcena = zbirSvihOcena + ocenaEntity.getOcena();
					}

				}
				prosecnaOcena = (double) zbirSvihOcena / brojOcena;

				zakljucnaOcena = (int) Math.round(prosecnaOcena);

				OcenaEntity zakljucnaOcenaPolugodiste = new OcenaEntity();

				zakljucnaOcenaPolugodiste.setDatumOcene(new Date());
				zakljucnaOcenaPolugodiste.setOcena(zakljucnaOcena);
				zakljucnaOcenaPolugodiste.setTipOcene(ETipOcene.POLUGODISTE);
				zakljucnaOcenaPolugodiste.setPredmet(pno.getPredajePredmet().getImePredmeta());
				zakljucnaOcenaPolugodiste.setNastavnik(
						pno.getNastavnik().getImeNastavnika() + " " + pno.getNastavnik().getPrezimeNastavnika());
				zakljucnaOcenaPolugodiste
						.setPunoImeUcenika(ucenikEntity.getImeUcenika() + " " + ucenikEntity.getPrezimeUcenika());
				zakljucnaOcenaPolugodiste.setIdUcenika(ucenikEntity.getIdUcenika());
				zakljucnaOcenaPolugodiste.setOcenioPredmetniNastavnik(pno);
				zakljucnaOcenaPolugodiste.setOcenaUcenika(ucenikEntity);
				zakljucnaOcenaPolugodiste.setSkolskaGodinaOcene(skolskaGodinaOcene);

				ocenaRepo.save(zakljucnaOcenaPolugodiste); //ovde je pukao

				sveZakljucneOcenePolugodista.add(zakljucnaOcenaPolugodiste);

			}
		}
		return new ResponseEntity<Iterable<OcenaEntity>>(sveZakljucneOcenePolugodista, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> zakljucivanjeOcenaZavrsnih(String skolskaGodinaOcene) {

		List<UcenikEntity> sviUcenici = (List<UcenikEntity>) ucenikRepo.findAll();

		Double prosecnaOcena = 0.;
		Integer zakljucnaOcena = 0;
		Integer brojOcena = 0;
		Integer zbirSvihOcena = 0;

		List<OcenaEntity> sveZakljucneOcenePolugodista = new ArrayList<>();

		for (UcenikEntity ucenikEntity : sviUcenici) {

			List<PredNastOdeljenjaEntity> predmeti = new ArrayList<>();

			predmeti = ucenikEntity.getOdeljenjeUcenika().getPredmetniNastavniciOdeljenja();

			for (PredNastOdeljenjaEntity pno : predmeti) {

				List<OcenaEntity> ocenePredmeta = pno.getOcene();

				zbirSvihOcena = 0;
				brojOcena = 0;

				for (OcenaEntity ocenaEntity : ocenePredmeta) {

					if (ocenaEntity.getSkolskaGodinaOcene().equals(skolskaGodinaOcene)) {

						brojOcena++;

						zbirSvihOcena = zbirSvihOcena + ocenaEntity.getOcena();
					}

				}
				prosecnaOcena = (double) zbirSvihOcena/brojOcena;

				zakljucnaOcena = (int) Math.round(prosecnaOcena);

				OcenaEntity zakljucnaOcenaPolugodiste = new OcenaEntity();

				zakljucnaOcenaPolugodiste.setDatumOcene(new Date());
				zakljucnaOcenaPolugodiste.setOcena(zakljucnaOcena);
				zakljucnaOcenaPolugodiste.setTipOcene(ETipOcene.ZAKLJUCNA);
				zakljucnaOcenaPolugodiste.setPredmet(pno.getPredajePredmet().getImePredmeta());
				zakljucnaOcenaPolugodiste.setNastavnik(
						pno.getNastavnik().getImeNastavnika() + " " + pno.getNastavnik().getPrezimeNastavnika());
				zakljucnaOcenaPolugodiste
						.setPunoImeUcenika(ucenikEntity.getImeUcenika() + " " + ucenikEntity.getPrezimeUcenika());
				zakljucnaOcenaPolugodiste.setIdUcenika(ucenikEntity.getIdUcenika());
				zakljucnaOcenaPolugodiste.setOcenioPredmetniNastavnik(pno);
				zakljucnaOcenaPolugodiste.setOcenaUcenika(ucenikEntity);
				zakljucnaOcenaPolugodiste.setSkolskaGodinaOcene(skolskaGodinaOcene);

				ocenaRepo.save(zakljucnaOcenaPolugodiste);

				sveZakljucneOcenePolugodista.add(zakljucnaOcenaPolugodiste);

			}
		}
		return new ResponseEntity<Iterable<OcenaEntity>>(sveZakljucneOcenePolugodista, HttpStatus.OK);
	}

}
