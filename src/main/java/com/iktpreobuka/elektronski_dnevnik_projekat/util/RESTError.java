package com.iktpreobuka.elektronski_dnevnik_projekat.util;

public class RESTError {

	//@JsonView(Views.Public.class)
	private String message;
	
	public RESTError(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
