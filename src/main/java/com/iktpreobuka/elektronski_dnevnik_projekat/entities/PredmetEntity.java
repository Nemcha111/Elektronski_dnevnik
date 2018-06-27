package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Predmeti")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PredmetEntity {

	@GeneratedValue
	@Id
	private Integer idPredmeta;

	@Column
	//TODO kreirati patern za unos imena (rec + jednocifren broj)
	@NotNull(message = "Ime predmeta mora biti uneto.")
	private String imePredmeta;

	@Column
	@Max(value = 5, message = "Nedeljni fond casova mora biti unet kao jednocifren broj izmedju 1 i 5.")
	@Min(value = 1, message = "Nedeljni fond casova mora biti unet kao jednocifren broj izmedju 1 i 5.")
	private Integer nedeljniFondCasova;
	
	
	@ManyToOne (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn (name = "razred")
	private RazredEntity razred;
	
	
	@JsonIgnore
	@OneToMany (mappedBy= "predajePredmet", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<PredNastOdeljenjaEntity> predmetniNastavnici;
	

	public PredmetEntity() {
		super();
	}


	public Integer getIdPredmeta() {
		return idPredmeta;
	}


	public void setIdPredmeta(Integer idPredmeta) {
		this.idPredmeta = idPredmeta;
	}


	public String getImePredmeta() {
		return imePredmeta;
	}


	public void setImePredmeta(String imePredmeta) {
		this.imePredmeta = imePredmeta;
	}


	public Integer getNedeljniFondCasova() {
		return nedeljniFondCasova;
	}


	public void setNedeljniFondCasova(Integer nedeljniFondCasova) {
		this.nedeljniFondCasova = nedeljniFondCasova;
	}


	public RazredEntity getRazred() {
		return razred;
	}


	public void setRazred(RazredEntity razred) {
		this.razred = razred;
	}


	public List<PredNastOdeljenjaEntity> getPredmetniNastavnici() {
		return predmetniNastavnici;
	}


	public void setPredmetniNastavnici(List<PredNastOdeljenjaEntity> predmetniNastavnici) {
		this.predmetniNastavnici = predmetniNastavnici;
	}



	

}
