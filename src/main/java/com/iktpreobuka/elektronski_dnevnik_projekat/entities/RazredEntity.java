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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name ="Razredi")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class RazredEntity {
	
	
	@GeneratedValue
	@Id
	private Integer idRazreda;
	
	@Column
//	@NotNull(message = "razred mora biti unet.")
	@Max(value = 8, message = "Razred mora biti unet kao jednocifreni broj izmedju 1 i 8.")
	@Min(value = 1, message = "Razred mora biti unet kao jednocifreni broj izmedju 1 i 8.")
	private Integer razred;
	
	//TODO probaj da dodas polja INTEGER pocetak i karaj skolske godine
	//a da skolska godina spaja ta dva polja sa kosom crtom izmedju
	
	//@JsonIgnore
	@Column
	//@NotNull(message = "Pocetak skolske godine mora biti unet.")
	private Integer pocetakSkolskeGodine;
	
	
	@Column
	//@NotNull(message = "Skolska godina mora biti uneta.")
	@Pattern(regexp = "^20([0-9]{2})\\/20([0-9]{2})$", message = "Skolska godina mora biti uneta u formi \"godina/godina\". Godina mora pocinjati sa 20.")  
	private String skolskaGodinaRazreda;
	
	
	@JsonIgnore
	@OneToMany (mappedBy= "razredOdeljenja", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<OdeljenjeEntity> odeljenja;
	
	
	@JsonIgnore
	@OneToMany (mappedBy= "razred", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<PredmetEntity> predmeti;
	

	public RazredEntity() {
		super();
	}


	public Integer getIdRazreda() {
		return idRazreda;
	}


	public void setIdRazreda(Integer idRazreda) {
		this.idRazreda = idRazreda;
	}


	public Integer getRazred() {
		return razred;
	}


	public void setRazred(Integer razred) {
		this.razred = razred;
	}

	

	public Integer getPocetakSkolskeGodine() {
		return pocetakSkolskeGodine;
	}


	public void setPocetakSkolskeGodine(Integer pocetakSkolskeGodine) {
		this.pocetakSkolskeGodine = pocetakSkolskeGodine;
	}


	public String getSkolskaGodinaRazreda() {
		return skolskaGodinaRazreda;
	}


	public void setSkolskaGodinaRazreda(String skolskaGodinaRazreda) {
		this.skolskaGodinaRazreda = skolskaGodinaRazreda;
	}


	public List<OdeljenjeEntity> getOdeljenja() {
		return odeljenja;
	}


	public void setOdeljenja(List<OdeljenjeEntity> odeljenja) {
		this.odeljenja = odeljenja;
	}


	public List<PredmetEntity> getPredmeti() {
		return predmeti;
	}


	public void setPredmeti(List<PredmetEntity> predmeti) {
		this.predmeti = predmeti;
	}


	@Override
	public String toString() {
		return "RazredEntity [idRazreda=" + idRazreda + ", razred=" + razred + ", pocetakSkolskeGodine="
				+ pocetakSkolskeGodine + ", skolskaGodinaRazreda=" + skolskaGodinaRazreda + ", odeljenja=" + odeljenja
				+ ", predmeti=" + predmeti + "]";
	}


	
	
	




}
