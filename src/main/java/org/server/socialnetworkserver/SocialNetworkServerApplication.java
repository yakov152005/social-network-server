package org.server.socialnetworkserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocialNetworkServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkServerApplication.class, args);
	}

}
