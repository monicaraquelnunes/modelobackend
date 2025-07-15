package br.com.controle_user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ControleUserApplication {

	public static void main(String[] args) {

		SpringApplication.run(ControleUserApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			System.out.println("Aplicação está rodando...");
		};
	}

}
