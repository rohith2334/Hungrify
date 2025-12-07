package com.app.hungrify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.app.hungrify.common.models","com.app.hungrify.main.models"})
@EnableJpaRepositories(basePackages = {"com.app.hungrify.common.repository","com.app.hungrify.main.repository"})
public class HungrifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HungrifyApplication.class, args);
	}
// test
}
