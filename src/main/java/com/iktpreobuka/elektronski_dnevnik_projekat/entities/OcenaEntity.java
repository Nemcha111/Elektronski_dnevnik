package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipOcene;

@Entity
@Table (name ="Ocene")
public class OcenaEntity {
	
	@GeneratedValue
	@Id
	private Integer idOcene;
	
	
	@Column
	@NotNull(message = "Ocena mora biti uneta.")
	@Max(value = 5, message = "Ocena mora biti unet kao jednocifreni broj izmedju 1 i 5.")
	@Min(value = 0, message = "Ocena mora biti unet kao jednocifreni broj izmedju 1 i 5.")
	private Integer ocena;
	
	@Column
	private Date datumOcene;
	
	@Column
	private ETipOcene tipOcene;
	
	@Column
	private String predmet;
	
	@Column
	private String nastavnik;
	
	@Column (name = "Ime_ucenika")
	private String punoImeUcenika;
	
	@Column
	private Integer idUcenika;
	
	@Column
	private String skolskaGodinaOcene;
	
	
	
	@JsonIgnore
	@ManyToOne (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn (name = "ocenaUcenika")
	private UcenikEntity ocenaUcenika;
	
	
	@JsonIgnore
	@ManyToOne (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn (name = "ocenioPredmetniNastavnik")
	private PredNastOdeljenjaEntity ocenioPredmetniNastavnik;
	

	public OcenaEntity() {
		super();
	}


	public Integer getIdOcene() {
		return idOcene;
	}


	public void setIdOcene(Integer idOcene) {
		this.idOcene = idOcene;
	}


	public Integer getOcena() {
		return ocena;
	}


	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}


	public Date getDatumOcene() {
		return datumOcene;
	}


	public void setDatumOcene(Date datumOcene) {
		this.datumOcene = datumOcene;
	}


	public ETipOcene getTipOcene() {
		return tipOcene;
	}


	public void setTipOcene(ETipOcene tipOcene) {
		this.tipOcene = tipOcene;
	}


	public String getPredmet() {
		return predmet;
	}


	public void setPredmet(String predmet) {
		this.predmet = predmet;
	}


	public String getNastavnik() {
		return nastavnik;
	}


	public void setNastavnik(String nastavnik) {
		this.nastavnik = nastavnik;
	}


	public String getPunoImeUcenika() {
		return punoImeUcenika;
	}


	public void setPunoImeUcenika(String punoImeUcenika) {
		this.punoImeUcenika = punoImeUcenika;
	}


	public Integer getIdUcenika() {
		return idUcenika;
	}


	public void setIdUcenika(Integer idUcenika) {
		this.idUcenika = idUcenika;
	}


	public UcenikEntity getOcenaUcenika() {
		return ocenaUcenika;
	}


	public void setOcenaUcenika(UcenikEntity ocenaUcenika) {
		this.ocenaUcenika = ocenaUcenika;
	}


	public PredNastOdeljenjaEntity getOcenioPredmetniNastavnik() {
		return ocenioPredmetniNastavnik;
	}


	public void setOcenioPredmetniNastavnik(PredNastOdeljenjaEntity ocenioPredmetniNastavnik) {
		this.ocenioPredmetniNastavnik = ocenioPredmetniNastavnik;
	}
	
	
	


	public String getSkolskaGodinaOcene() {
		return skolskaGodinaOcene;
	}


	public void setSkolskaGodinaOcene(String skolskaGodinaOcene) {
		this.skolskaGodinaOcene = skolskaGodinaOcene;
	}


	@Override
	public String toString() {
		return "OcenaEntity [idOcene=" + idOcene + ", ocena=" + ocena + ", datumOcene=" + datumOcene + ", tipOcene="
				+ tipOcene + ", predmet=" + predmet + ", nastavnik=" + nastavnik + ", punoImeUcenika=" + punoImeUcenika
				+ ", idUcenika=" + idUcenika + "]";
	}



	

	

}
