package com.upendra.url.service;

import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude = RedisRepositoriesAutoConfiguration.class)
@EnableCaching
public class URLShortenerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(URLShortenerServiceApplication.class, args);
	}
	
	@PostConstruct
	public void init() {
	    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
