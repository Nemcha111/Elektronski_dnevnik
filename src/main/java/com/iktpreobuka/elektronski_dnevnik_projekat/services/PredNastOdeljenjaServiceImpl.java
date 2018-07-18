package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.PredNastOdeljenjaEntity;


@Service
public class PredNastOdeljenjaServiceImpl implements PredNastOdeljenjaService {

	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<PredNastOdeljenjaEntity> prikaziPredmeteNastavnika(Integer idNastavnika) {
		String sql = "select predaje_predmet " + 
					"from predmetni_nastavnici_odeljenja pno" + 
					"where pno.nastavnik = :idNastavnika ";
		Query query = em.createQuery(sql);
		query.setParameter("idNastavnika", idNastavnika);
		List<PredNastOdeljenjaEntity> result = new ArrayList<>();
		result = query.getResultList();
		return result;
	}

}
