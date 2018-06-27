package com.iktpreobuka.elektronski_dnevnik_projekat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.RazredEntity;

public interface RazredRepository extends CrudRepository<RazredEntity, Integer> {

	List<RazredEntity>findBySkolskaGodinaRazreda(String skolskaGodinaRazreda);
	
}
