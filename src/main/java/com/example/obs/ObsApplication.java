package com.example.obs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.obs")
public class ObsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObsApplication.class, args);
	}

}
