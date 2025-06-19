package net.hovering.Hovering_Web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "net.hovering.Hovering_Web")
public class HoveringWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(HoveringWebApplication.class, args);
	}
}