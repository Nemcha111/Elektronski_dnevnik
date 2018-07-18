package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipKorisnika;

@Entity
@Table(name = "Administratori")
public class AdminEntity {

	@GeneratedValue
	@Id
	private Integer idAdmina;

	@Column
	@NotNull(message = "Korisnicko ime mora biti uneto.")
	@Size(min = 2, max = 30, message = "Korisnicko ime mora biti izmedju {min} i {max} karaktera.")
	private String korisnickoImeAdmina;

	@Column
	@NotNull(message = "Sifra mora biti uneta.")
	//@Size(min = 2, max = 80, message = "Sifra mora biti izmedju {min} i {max} karaktera.")
	private String sifraAdmina;

	@Column (name = "uloga_admina")
	private ETipKorisnika korisnikAdmina = ETipKorisnika.ROLE_ADMIN;

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

	public ETipKorisnika getKorisnikAdmina() {
		return korisnikAdmina;
	}

	public void setKorisnikAdmina(ETipKorisnika korisnikAdmina) {
		this.korisnikAdmina = korisnikAdmina;
	}

	@Override
	public String toString() {
		return "AdminEntity [idAdmina=" + idAdmina + ", korisnickoImeAdmina=" + korisnickoImeAdmina + ", sifraAdmina="
				+ sifraAdmina + ", korisnikAdmina=" + korisnikAdmina + "]";
	}

}
