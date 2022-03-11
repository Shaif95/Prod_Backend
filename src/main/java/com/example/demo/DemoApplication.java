package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableMongoRepositories
@EnableMongoAuditing
@EnableScheduling
public class DemoApplication {

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	@GetMapping("/new")
	public String Backend()
	{
		return "Welcome to Tweetsy Backend Spring";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

}
