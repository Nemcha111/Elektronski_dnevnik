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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	@NotNull(message = "Ime mora biti uneto.")
	@Size(min = 2, max = 30, message = "Ime mora biti izmedju {min} i {max} karaktera.")
	private String imeNastavnika;

	
	@Column
	@NotNull(message = "Prezime mora biti uneto.")
	@Size(min = 2, max = 30, message = "Prezime mora biti izmedju {min} i {max} karaktera.")
	private String prezimeNastavnika;

	
	@Column (name = "korisnickoImeNastavnika")
	@NotNull(message = "Korisnicko ime mora biti uneto.")
	@Size(min = 5, max = 20, message = "Korisnicko ime mora biti izmedju {min} i {max} karaktera.")
	private String korisnickoImeNastavnika;

	
	@Column
	@NotNull(message = "Sifra mora biti uneta.")
	//@Size(min = 5, max = 20, message = "Sifra mora biti izmedju {min} i {max} karaktera.")
	private String sifraNastavnika;
	
	@Column (name = "uloga_nastavnika")
	private ETipKorisnika korisnikNastavnik = ETipKorisnika.ROLE_NASTAVNIK;
	
	
	@JsonIgnore
	@OneToMany (mappedBy= "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<PredNastOdeljenjaEntity> predmetniNastavniciOdeljenja;
	
//	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	@JoinColumn(name = "role")
//	private RoleEntity role;


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


//	public RoleEntity getRole() {
//		return role;
//	}
//
//
//	public void setRole(RoleEntity role) {
//		this.role = role;
//	}


	@Override
	public String toString() {
		return "NastavnikEntity [idNastavnika=" + idNastavnika + ", imeNastavnika=" + imeNastavnika
				+ ", prezimeNastavnika=" + prezimeNastavnika + ", korisnickoImeNastavnika=" + korisnickoImeNastavnika
				+ ", sifraNastavnika=" + sifraNastavnika 
				+ ", predmetniNastavniciOdeljenja=" + predmetniNastavniciOdeljenja + "]";
	}

	
	
}