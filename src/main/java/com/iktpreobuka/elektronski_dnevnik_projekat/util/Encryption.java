package com.iktpreobuka.elektronski_dnevnik_projekat.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {

	public static String getPassEncoded(String pass) {
			
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(pass);
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println(getPassEncoded("admin001"));
//
//	}

}
