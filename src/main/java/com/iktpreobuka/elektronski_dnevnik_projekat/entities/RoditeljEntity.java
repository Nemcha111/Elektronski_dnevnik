package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipKorisnika;

@Entity
@Table(name = "Roditelji")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RoditeljEntity {

	@GeneratedValue
	@Id
	private Integer idRoditelja;

	@Column
	@NotNull(message = "Ime roditelja mora biti uneto.")
	@Size(min = 2, max = 30, message = "Ime mora biti izmedju {min} i {max} karaktera.")
	private String imeRoditelja;

	@Column
	@NotNull(message = "Prezime mora biti uneto.")
	@Size(min = 2, max = 30, message = "Prezime mora biti izmedju {min} i {max} karaktera.")
	private String prezimeRoditelja;

	@Column
	@NotNull(message = "Korisnicko ime mora biti uneto.")
	@Size(min = 5, max = 20, message = "Korisnicko ime mora biti izmedju {min} i {max} karaktera.")
	private String korisnickoImeRoditelja;

	@Column
	@NotNull(message = "Sifra mora biti uneta.")
	//@Size(min = 5, max = 50, message = "Sifra mora biti izmedju {min} i {max} karaktera.")
	private String sifraRoditelja;

	@Column
	@NotNull(message = "Email mora biti unet.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") 
	private String emailRoditelja;

	@Column
	private ETipKorisnika ulogaRoditelja = ETipKorisnika.ROLE_RODITELJ;

	@JsonIgnore
	@OneToMany(mappedBy = "roditelj", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<UcenikEntity> deca;

	public RoditeljEntity() {
		super();
	}

	public Integer getIdRoditelja() {
		return idRoditelja;
	}

	public void setIdRoditelja(Integer idRoditelja) {
		this.idRoditelja = idRoditelja;
	}

	public String getImeRoditelja() {
		return imeRoditelja;
	}

	public void setImeRoditelja(String imeRoditelja) {
		this.imeRoditelja = imeRoditelja;
	}

	public String getPrezimeRoditelja() {
		return prezimeRoditelja;
	}

	public void setPrezimeRoditelja(String prezimeRoditelja) {
		this.prezimeRoditelja = prezimeRoditelja;
	}

	public String getKorisnickoImeRoditelja() {
		return korisnickoImeRoditelja;
	}

	public void setKorisnickoImeRoditelja(String korisnickoImeRoditelja) {
		this.korisnickoImeRoditelja = korisnickoImeRoditelja;
	}

	public String getSifraRoditelja() {
		return sifraRoditelja;
	}

	public void setSifraRoditelja(String sifraRoditelja) {
		this.sifraRoditelja = sifraRoditelja;
	}

	public String getEmailRoditelja() {
		return emailRoditelja;
	}

	public void setEmailRoditelja(String emailRoditelja) {
		this.emailRoditelja = emailRoditelja;
	}

	public List<UcenikEntity> getDeca() {
		return deca;
	}

	public void setDeca(List<UcenikEntity> deca) {
		this.deca = deca;
	}

	public ETipKorisnika getUlogaRoditelja() {
		return ulogaRoditelja;
	}

	public void setUlogaRoditelja(ETipKorisnika korisniRoditelj) {
		this.ulogaRoditelja = korisniRoditelj;
	}

}
