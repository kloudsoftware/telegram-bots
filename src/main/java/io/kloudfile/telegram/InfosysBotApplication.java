package io.kloudfile.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.hibernate.cfg.Configuration;


@SpringBootApplication
@EnableScheduling
public class InfosysBotApplication {

	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");

		SpringApplication.run(InfosysBotApplication.class, args);
	}
}
