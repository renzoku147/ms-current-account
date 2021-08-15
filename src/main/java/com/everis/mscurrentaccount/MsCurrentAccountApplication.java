package com.everis.mscurrentaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsCurrentAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCurrentAccountApplication.class, args);
	}

}
