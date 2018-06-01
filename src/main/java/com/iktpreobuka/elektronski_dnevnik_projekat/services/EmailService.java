package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OcenaEntity;

public interface EmailService {
	
	void sendTemplateMessage (OcenaEntity ocena) throws Exception;
	
}
