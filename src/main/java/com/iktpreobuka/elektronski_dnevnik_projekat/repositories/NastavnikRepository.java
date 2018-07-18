package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.NastavnikEntity;


public interface NastavnikRepository extends CrudRepository<NastavnikEntity, Integer> {
	
	List<NastavnikEntity> findByImeNastavnika (String imeNastavnika);
	
	List<NastavnikEntity> findByPrezimeNastavnika (String prezimeNastavnika);
	
	NastavnikEntity findByKorisnickoImeNastavnika (String korisnickoImeNastavnika);

}
