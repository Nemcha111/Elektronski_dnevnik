package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Predmetni_nastavnici_odeljenja")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PredNastOdeljenjaEntity { // PredNastOdeljenja = predmetni nastavnik odeljenja

	@GeneratedValue
	@Id
	private Integer idPredNastOdeljenja;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predajePredmet")
	private PredmetEntity predajePredmet;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predajeOdeljenju")
	private OdeljenjeEntity predajeOdeljenju;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nastavnik")
	private NastavnikEntity nastavnik;

	@JsonIgnore
	@OneToMany(mappedBy = "ocenioPredmetniNastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<OcenaEntity> ocene;

	public PredNastOdeljenjaEntity() {
		super();
	}

	public Integer getIdPredNastOdeljenja() {
		return idPredNastOdeljenja;
	}

	public void setIdPredNastOdeljenja(Integer idPredNastOdeljenja) {
		this.idPredNastOdeljenja = idPredNastOdeljenja;
	}

	public PredmetEntity getPredajePredmet() {
		return predajePredmet;
	}

	public void setPredajePredmet(PredmetEntity predajePredmet) {
		this.predajePredmet = predajePredmet;
	}

	public OdeljenjeEntity getPredajeOdeljenju() {
		return predajeOdeljenju;
	}

	public void setPredajeOdeljenju(OdeljenjeEntity predajeOdeljenju) {
		this.predajeOdeljenju = predajeOdeljenju;
	}

	public NastavnikEntity getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(NastavnikEntity nastavnik) {
		this.nastavnik = nastavnik;
	}

	public List<OcenaEntity> getOcene() {
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene) {
		this.ocene = ocene;
	}



}
