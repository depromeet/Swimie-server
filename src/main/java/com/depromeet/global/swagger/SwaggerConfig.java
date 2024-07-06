package com.depromeet.global.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
	private static final String API_TITLE = "Depromeet Team2 Backend API Docs";
	private static final String API_DESCRIPTION = "Depromeet Team2 Backend API 문서입니다.";

	private final SwaggerProperties swaggerProperties;

	@Bean
	public OpenAPI openAPI() {
		Info info =
				new Info()
						.version(swaggerProperties.version())
						.title(API_TITLE)
						.description(API_DESCRIPTION);

		return new OpenAPI()
				.info(info)
				.addSecurityItem(getSecurityRequirement())
				.components(getAuthComponent());
	}

	private SecurityRequirement getSecurityRequirement() {
		String jwt = "JWT";
		return new SecurityRequirement().addList(jwt);
	}

	private Components getAuthComponent() {
		return new Components()
				.addSecuritySchemes(
						"JWT",
						new SecurityScheme()
								.name("JWT")
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.in(SecurityScheme.In.HEADER)
								.name("Authorization"));
	}
}
