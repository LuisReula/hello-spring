package com.sinensia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoProjectApplication.class, args);
	}

	@GetMapping("/hello")
	public String Hello(@RequestParam(value = "name", defaultValue = "World") String Name){
		return String.format("Hello %s!", Name);
	}

	@GetMapping("/testing")
	public String Testing(@RequestParam(value = "test", defaultValue = "a todos") String Name){
		return String.format("Hola %s!", Name);
	}

}
