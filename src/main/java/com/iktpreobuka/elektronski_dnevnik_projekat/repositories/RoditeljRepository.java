package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;



import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;


public interface RoditeljRepository extends CrudRepository<RoditeljEntity, Integer> {
	
	RoditeljEntity findByKorisnickoImeRoditelja (String korisnickoIme);
	
	List<RoditeljEntity> findByImeRoditelja (String imeRoditelja);
	
	List<RoditeljEntity> findByPrezimeRoditelja (String prezimeRoditelja);
	
}
