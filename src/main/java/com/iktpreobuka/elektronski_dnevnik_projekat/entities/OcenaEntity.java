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

import com.iktpreobuka.elektronski_dnevnik_projekat.enumerations.ETipOcene;

@Entity
@Table (name ="Ocene")
public class OcenaEntity {
	
	@GeneratedValue
	@Id
	private Integer idOcene;
	
	
	@Column
	private Integer ocena;
	
	@Column
	private Date datumOcene;
	
	@Column
	private ETipOcene tipOcene;
	
	
	@ManyToOne (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn (name = "ocenaUcenika")
	private UcenikEntity ocenaUcenika;
	
	
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


	@Override
	public String toString() {
		return "OcenaEntity [idOcene=" + idOcene + ", ocena=" + ocena + ", datumOcene=" + datumOcene + ", tipOcene="
				+ tipOcene + ", ocenaUcenika=" + ocenaUcenika + ", ocenioPredmetniNastavnik=" + ocenioPredmetniNastavnik
				+ "]";
	}

	

}
