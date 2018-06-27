package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OcenaEntity;

public interface OcenaRepository extends CrudRepository<OcenaEntity, Integer> {
	
	
	List<OcenaEntity>findBySkolskaGodinaOcene(String skolskaGodinaOcene);
	

}
