package com.example.StockPicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages= {"com.example.StockPickerController"})
@ComponentScan(basePackages= {"com.example.StockPickerFetch"})
@ComponentScan(basePackages= {"com.example.StockScalperFetch"})
public class StockPickerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPickerApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(
								"http://localhost:3000",
								"https://669ca762ec4953971cb32f82--magnificent-marshmallow-d2bdb6.netlify.app"
						)
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}
