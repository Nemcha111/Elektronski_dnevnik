package com.iktpreobuka.elektronski_dnevnik_projekat.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic()
				.authenticationEntryPoint(authEntryPoint);
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().usersByUsernameQuery(nastavniciQuery).authoritiesByUsernameQuery(roleNastavniciQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource).and()
				.jdbcAuthentication().usersByUsernameQuery(adminiQuery).authoritiesByUsernameQuery(roleAdminiQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource).and()
				.jdbcAuthentication().usersByUsernameQuery(roditeljQuery).authoritiesByUsernameQuery(roleRoditeljiQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource).and()
				.jdbcAuthentication().usersByUsernameQuery(ucenikQuery).authoritiesByUsernameQuery(roleUceniciQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource);
	}

	@Value("${spring.queries.nastavnici-query}")
	private String nastavniciQuery;

	@Value("${spring.queries.rolesNastavnici-query}")
	private String roleNastavniciQuery;
	
	@Value("${spring.queries.admini-query}")
	private String adminiQuery;

	@Value("${spring.queries.rolesAdmini-query}")
	private String roleAdminiQuery;
	
	@Value("${spring.queries.roditelj-query}")
	private String roditeljQuery;

	@Value("${spring.queries.rolesRoditelji-query}")
	private String roleRoditeljiQuery;
	
	@Value("${spring.queries.ucenik-query}")
	private String ucenikQuery;

	@Value("${spring.queries.rolesUcenici-query}")
	private String roleUceniciQuery;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
