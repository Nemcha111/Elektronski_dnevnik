package com.iktpreobuka.elektronski_dnevnik_projekat.entities;
//
import java.util.List;
//
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipKorisnika;


@Entity
@Table (name ="nastavnici")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class NastavnikEntity {

	@GeneratedValue
	@Id
	private Integer idNastavnika;

	
	@Column
	private String imeNastavnika;

	
	@Column
	private String prezimeNastavnika;

	
	@Column
	private String korisnickoImeNastavnika;

	
	@Column
	private String sifraNastavnika;
	
	@Column
	private ETipKorisnika korisnikNastavnik = ETipKorisnika.ROLE_NASTAVNIK;
	
	@JsonIgnore
	@OneToMany (mappedBy= "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<PredNastOdeljenjaEntity> predmetniNastavniciOdeljenja;


	public NastavnikEntity() {
		super();
	}


	public Integer getIdNastavnika() {
		return idNastavnika;
	}


	public void setIdNastavnika(Integer idNastavnika) {
		this.idNastavnika = idNastavnika;
	}


	public String getImeNastavnika() {
		return imeNastavnika;
	}


	public void setImeNastavnika(String imeNastavnika) {
		this.imeNastavnika = imeNastavnika;
	}


	public String getPrezimeNastavnika() {
		return prezimeNastavnika;
	}


	public void setPrezimeNastavnika(String prezimeNastavnika) {
		this.prezimeNastavnika = prezimeNastavnika;
	}


	public String getKorisnickoImeNastavnika() {
		return korisnickoImeNastavnika;
	}


	public void setKorisnickoImeNastavnika(String korisnickoImeNastavnika) {
		this.korisnickoImeNastavnika = korisnickoImeNastavnika;
	}


	public String getSifraNastavnika() {
		return sifraNastavnika;
	}


	public void setSifraNastavnika(String sifraNastavnika) {
		this.sifraNastavnika = sifraNastavnika;
	}


	public List<PredNastOdeljenjaEntity> getPredmetniNastavniciOdeljenja() {
		return predmetniNastavniciOdeljenja;
	}


	public void setPredmetniNastavniciOdeljenja(List<PredNastOdeljenjaEntity> predmetniNastavniciOdeljenja) {
		this.predmetniNastavniciOdeljenja = predmetniNastavniciOdeljenja;
	}


	public ETipKorisnika getKorisnikNastavnik() {
		return korisnikNastavnik;
	}


	public void setKorisnikNastavnik(ETipKorisnika korisnikNastavnik) {
		this.korisnikNastavnik = korisnikNastavnik;
	}


	@Override
	public String toString() {
		return "NastavnikEntity [idNastavnika=" + idNastavnika + ", imeNastavnika=" + imeNastavnika
				+ ", prezimeNastavnika=" + prezimeNastavnika + ", korisnickoImeNastavnika=" + korisnickoImeNastavnika
				+ ", sifraNastavnika=" + sifraNastavnika + ", korisnikNastavnik=" + korisnikNastavnik
				+ ", predmetniNastavniciOdeljenja=" + predmetniNastavniciOdeljenja + "]";
	}

	
	
}