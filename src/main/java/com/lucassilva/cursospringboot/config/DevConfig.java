package com.lucassilva.cursospringboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.lucassilva.cursospringboot.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	public DBService dbService;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instantiateDatabase() throws Exception {

		if (!strategy.equals("create")) {
			return false;
		}
		
		dbService.instantiateTestDatabase();
		return true;
	}

}
