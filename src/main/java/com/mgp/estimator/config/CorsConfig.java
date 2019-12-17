package com.mgp.estimator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**").allowedOrigins("http://localhost:3000","http://localhost:8080","http://10.254.0.234:8080","http://10.254.5.250:3000")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "ORIGIN").allowCredentials(true);
	}

}