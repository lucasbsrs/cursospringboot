package com.lucassilva.cursospringboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.lucassilva.cursospringboot.services.DBService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	public DBService dbService;

	@Bean
	public boolean instantiateDatabase() throws Exception {
		dbService.instantiateTestDatabase();
		return true;
	}

}
