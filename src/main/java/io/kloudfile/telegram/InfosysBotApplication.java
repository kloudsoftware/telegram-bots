package io.kloudfile.telegram;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Properties;


@SpringBootApplication
@EnableScheduling
public class InfosysBotApplication {

	public static void main(String[] args) throws IOException {
		Properties props = new Properties();
		props.load(InfosysBotApplication.class.getResourceAsStream("/log4j.properties"));
		PropertyConfigurator.configure(props);

		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");

		SpringApplication.run(InfosysBotApplication.class, args);
	}
}
