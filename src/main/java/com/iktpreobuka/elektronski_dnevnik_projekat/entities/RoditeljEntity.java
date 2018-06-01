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
	private String prezimeRoditelja;

	@Column
	private String korisnickoImeRoditelja;

	@Column
	private String sifraRoditelja;

	@Column
	private String emailRoditelja;

	@Column
	private ETipKorisnika ulogaRoditelja = ETipKorisnika.ROLE_RODITELJ;

	//@JsonManagedReference
	//@JsonBackReference
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
