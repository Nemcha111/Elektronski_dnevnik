package com.iktpreobuka.elektronski_dnevnik_projekat.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "role") //ime tabele mora da bude isto kao u upitu u aplication.propertis
public class RoleEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="role_id")
	private Integer id;
	
	
	@Column(name="role_name")
	private String name;
	
	
//	@JsonIgnore
//	@OneToMany(mappedBy= "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
//	private List<NastavnikEntity> users = new ArrayList<>();

	
	public RoleEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public List<NastavnikEntity> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<NastavnikEntity> users) {
//		this.users = users;
//	}

	@Override
	public String toString() {
		return "RoleEntity [id=" + id + ", name=" + name +  "]";
	}
	
	

}
