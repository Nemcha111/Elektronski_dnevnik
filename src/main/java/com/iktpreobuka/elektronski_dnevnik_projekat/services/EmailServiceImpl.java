package com.iktpreobuka.elektronski_dnevnik_projekat.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronski_dnevnik_projekat.entities.OcenaEntity;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.RoditeljRepository;
import com.iktpreobuka.elektronski_dnevnik_projekat.repositories.UcenikRepository;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	UcenikRepository ucenikRepo;

	@Autowired
	RoditeljRepository roditeljRepo;

	@Override
	public void sendTemplateMessage(OcenaEntity ocena) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(ocena.getOcenaUcenika().getRoditelj().getEmailRoditelja());
		helper.setSubject("Nova ocena vaseg deteta");
		String text = "<html><body>" + "<table style='border:2px solid black'>"
				+ "<tr><th>Ucenik</th><th>Predmet</th><th>Nastavnik</th><th>Ocena</th><th>Datum ocenjivanja</th></tr>"
				+ "<tr><td>" + ocena.getOcenaUcenika().getImeUcenika() + " "
				+ ocena.getOcenaUcenika().getPrezimeUcenika() + "</td>" + 
				"<td>" + ocena.getOcenioPredmetniNastavnik().getPredajePredmet().getImePredmeta() + "</td>" + 
				"<td>" + ocena.getOcenioPredmetniNastavnik().getNastavnik().getImeNastavnika() + " "
				+ ocena.getOcenioPredmetniNastavnik().getNastavnik().getPrezimeNastavnika() + "</td>" + 
				"<td>"+ ocena.getOcena() + "</td>" + 
				"<td>" + ocena.getDatumOcene() +
				"</td></tr></table></body></html>";
		helper.setText(text, true);

		
			emailSender.send(mail);
		

	}

}
