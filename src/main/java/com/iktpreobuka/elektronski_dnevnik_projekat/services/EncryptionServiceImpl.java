package com.iktpreobuka.elektronski_dnevnik_projekat.services;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

	public static String getPassEncoded(String pass) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(pass);
	}
	
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println(getPassEncoded("markom001"));
//
//	}
	
	public String enkriptor (String sifra) {
		
		return getPassEncoded(sifra);
	}

}
