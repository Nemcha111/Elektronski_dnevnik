package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.NastavnikEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;


public interface PredNastOdeljenjaRepository extends CrudRepository<PredNastOdeljenjaEntity, Integer> {
	
	

	List<PredNastOdeljenjaEntity> findByNastavnik(NastavnikEntity nastavnik);
	

}
