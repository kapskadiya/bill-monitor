package com.kashyap.homeIdeas.billmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BillMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillMonitorApplication.class, args);
	}

}
