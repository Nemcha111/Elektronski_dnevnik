package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RoditeljEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.entities.UcenikEntity;

public interface UcenikRepository extends CrudRepository<UcenikEntity, Integer> {
	
//	@Query("select roditelj from ucenici where ")
//	Stream<User> findAllByCustomQueryAndStream();
	
	
	List<UcenikEntity> findByRoditelj (RoditeljEntity roditelj);

}
