package com.pharmacie.enligne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EnLigneApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnLigneApplication.class, args);
	}

}
