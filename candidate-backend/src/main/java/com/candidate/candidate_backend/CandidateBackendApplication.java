package com.candidate.candidate_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CandidateBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CandidateBackendApplication.class, args);
	}

}
