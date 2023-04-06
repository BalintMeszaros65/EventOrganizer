package com.codecool.eventorganizer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
	name = "Bearer Authentication",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	scheme = "bearer"
)
@SecurityScheme(
	name = "Basic Authentication",
	type = SecuritySchemeType.HTTP,
	scheme = "basic"
)
@OpenAPIDefinition(
	info = @Info(
		title = "Event Organizer API",
		version = "1.0",
		// TODO change the placeholders
		description = "Event Organizer placeholder description",
		termsOfService = "placeholder terms",
		contact = @Contact(
			name = "Bálint Mészáros",
			email = "balint.meszaros65@gmail.com"
		),
		license = @License(
			name = "Placeholder licence",
			url = "placeholder url"
		)
	)
)
// TODO implement lombok to get rid of boilerplate code
public class EventOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventOrganizerApplication.class, args);
	}

}
