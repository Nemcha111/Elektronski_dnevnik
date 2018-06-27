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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipKorisnika;

@Entity
@Table(name = "Ucenici")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UcenikEntity {

	@GeneratedValue
	@Id
	private Integer idUcenika;

	@Column
	@NotNull(message = "Ime ucenika mora biti uneto.")
	@Size(min = 2, max = 30, message = "Ime mora biti izmedju {min} i {max} karaktera.")
	private String imeUcenika;

	@Column
	@NotNull(message = "Prezime ucenika mora biti uneto.")
	@Size(min = 2, max = 30, message = "Prezime mora biti izmedju {min} i {max} karaktera.")
	private String prezimeUcenika;

	@Column
	@NotNull(message = "Korisnicko ime ucenika mora biti uneto.")
	@Size(min = 5, max = 20, message = "Korisnicko ime mora biti izmedju {min} i {max} karaktera.")
	private String korisnickoImeUcenika;

	@Column
	@NotNull(message = "Sifra ucenika mora biti uneto.")
	@Size(min = 5, max = 20, message = "Sifra mora biti izmedju {min} i {max} karaktera.")
	private String sifraUcenika;

	@Column
	private ETipKorisnika ulogaUcenika = ETipKorisnika.ROLE_UCENIK;


	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "roditelj")
	private RoditeljEntity roditelj;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenjeUcenika")
	private OdeljenjeEntity odeljenjeUcenika;

	@JsonIgnore
	@OneToMany(mappedBy = "ocenaUcenika", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<OcenaEntity> ocene;

	public UcenikEntity() {
		super();
	}

	public Integer getIdUcenika() {
		return idUcenika;
	}

	public void setIdUcenika(Integer idUcenika) {
		this.idUcenika = idUcenika;
	}

	public String getImeUcenika() {
		return imeUcenika;
	}

	public void setImeUcenika(String imeUcenika) {
		this.imeUcenika = imeUcenika;
	}

	public String getPrezimeUcenika() {
		return prezimeUcenika;
	}

	public void setPrezimeUcenika(String prezimeUcenika) {
		this.prezimeUcenika = prezimeUcenika;
	}

	public String getKorisnickoImeUcenika() {
		return korisnickoImeUcenika;
	}

	public void setKorisnickoImeUcenika(String korisnickoImeUcenika) {
		this.korisnickoImeUcenika = korisnickoImeUcenika;
	}

	public String getSifraUcenika() {
		return sifraUcenika;
	}

	public void setSifraUcenika(String sifraUcenika) {
		this.sifraUcenika = sifraUcenika;
	}

	public RoditeljEntity getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(RoditeljEntity roditelj) {
		this.roditelj = roditelj;
	}

	public OdeljenjeEntity getOdeljenjeUcenika() {
		return odeljenjeUcenika;
	}

	public void setOdeljenjeUcenika(OdeljenjeEntity odeljenjeUcenika) {
		this.odeljenjeUcenika = odeljenjeUcenika;
	}

	public List<OcenaEntity> getOcene() {
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene) {
		this.ocene = ocene;
	}

	public ETipKorisnika getKorisnikUcenik() {
		return ulogaUcenika;
	}

	public void setKorisnikUcenik(ETipKorisnika korisnikUcenik) {
		this.ulogaUcenika = korisnikUcenik;
	}


	

}
