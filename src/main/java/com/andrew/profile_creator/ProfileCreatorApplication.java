package com.andrew.profile_creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({"com"})
public class ProfileCreatorApplication {

	private static final Logger logger = LoggerFactory.getLogger(ProfileCreatorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProfileCreatorApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}


}
