package com.lucas.vacinakids;

import org.springframework.boot.SpringApplication;

public class TestVacinaKidsApplication {

	public static void main(String[] args) {
		SpringApplication.from(VacinaKidsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
