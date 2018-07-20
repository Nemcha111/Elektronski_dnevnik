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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Odeljenja")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OdeljenjeEntity {

	
	@GeneratedValue
	@Id
	private Integer idOdeljenja;

	
	@Column
	@NotNull(message = "Ime odeljenja mora biti uneto.")
	@Pattern(regexp = "^([a-j]{1})$", message = "Odeljenje mora biti uneto kao jedno malo slovo izmedju a i j.")
	private String imeOdeljenja;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razredOdeljenja")
	private RazredEntity razredOdeljenja;

	@JsonIgnore
	@OneToMany(mappedBy = "odeljenjeUcenika", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<UcenikEntity> ucenici;

	@JsonIgnore
	@OneToMany(mappedBy = "predajeOdeljenju", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<PredNastOdeljenjaEntity> predmetniNastavniciOdeljenja;

	public OdeljenjeEntity() {
		super();
	}

	public Integer getIdOdeljenja() {
		return idOdeljenja;
	}

	public void setIdOdeljenja(Integer idOdeljenja) {
		this.idOdeljenja = idOdeljenja;
	}

	public String getImeOdeljenja() {
		return imeOdeljenja;
	}

	public void setImeOdeljenja(String imeOdeljenja) {
		this.imeOdeljenja = imeOdeljenja;
	}


	public RazredEntity getRazredOdeljenja() {
		return razredOdeljenja;
	}

	public void setRazredOdeljenja(RazredEntity razredOdeljenja) {
		this.razredOdeljenja = razredOdeljenja;
	}

	public List<UcenikEntity> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<UcenikEntity> ucenici) {
		this.ucenici = ucenici;
	}

	public List<PredNastOdeljenjaEntity> getPredmetniNastavniciOdeljenja() {
		return predmetniNastavniciOdeljenja;
	}

	public void setPredmetniNastavniciOdeljenja(List<PredNastOdeljenjaEntity> predmetniNastavniciOdeljenja) {
		this.predmetniNastavniciOdeljenja = predmetniNastavniciOdeljenja;
	}

	@Override
	public String toString() {
		return "OdeljenjeEntity [idOdeljenja=" + idOdeljenja + ", imeOdeljenja=" + imeOdeljenja
				+ ", razredOdeljenja=" + razredOdeljenja + ", ucenici=" + ucenici
				+ ", predmetniNastavniciOdeljenja=" + predmetniNastavniciOdeljenja + "]";
	}

}
