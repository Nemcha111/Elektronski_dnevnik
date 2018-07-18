package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;

public interface UcenikRepository extends CrudRepository<UcenikEntity, Integer> {
	
	
	List<UcenikEntity> findByRoditelj (RoditeljEntity roditelj);
	
	List<UcenikEntity> findByImeUcenika (String imeRoditelja);
	
	List<UcenikEntity> findByPrezimeUcenika (String prezimeRoditelja);
	
	UcenikEntity findByKorisnickoImeUcenika (String korisnickoImeUcenika);

}
