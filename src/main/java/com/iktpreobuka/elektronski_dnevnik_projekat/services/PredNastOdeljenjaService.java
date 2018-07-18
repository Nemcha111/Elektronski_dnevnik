package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import java.util.List;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;

public interface PredNastOdeljenjaService {

	public List<PredNastOdeljenjaEntity> prikaziPredmeteNastavnika(Integer idNastavnika);
	
}
