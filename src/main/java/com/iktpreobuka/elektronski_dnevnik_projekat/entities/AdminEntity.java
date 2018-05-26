package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipKorisnika;

@Entity
@Table (name ="Administratori")
public class AdminEntity {
	
	
	@GeneratedValue
	@Id
	private Integer idAdmina;

	@Column
	private String korisnickoImeAdmina;

	
	@Column
	private String sifraAdmina;
	
	
	@Column
	private ETipKorisnika korisnikAdmin = ETipKorisnika.ROLE_ADMIN;


	public AdminEntity() {
		super();
	}


	public Integer getIdAdmina() {
		return idAdmina;
	}


	public void setIdAdmina(Integer idAdmina) {
		this.idAdmina = idAdmina;
	}


	public String getKorisnickoImeAdmina() {
		return korisnickoImeAdmina;
	}


	public void setKorisnickoImeAdmina(String korisnickoImeAdmina) {
		this.korisnickoImeAdmina = korisnickoImeAdmina;
	}


	public String getSifraAdmina() {
		return sifraAdmina;
	}


	public void setSifraAdmina(String sifraAdmina) {
		this.sifraAdmina = sifraAdmina;
	}


	public ETipKorisnika getKorisnikAdmin() {
		return korisnikAdmin;
	}


	public void setKorisnikAdmin(ETipKorisnika korisnikAdmin) {
		this.korisnikAdmin = korisnikAdmin;
	}


	@Override
	public String toString() {
		return "AdminEntity [idAdmina=" + idAdmina + ", korisnickoImeAdmina=" + korisnickoImeAdmina + ", sifraAdmina="
				+ sifraAdmina + ", korisnikAdmin=" + korisnikAdmin + "]";
	}


}
