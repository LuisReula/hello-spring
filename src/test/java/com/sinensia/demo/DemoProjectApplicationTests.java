package com.sinensia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoProjectApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void rootTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/", String.class)).isEqualTo("Bienvenidos a mi primera prueba");
	}

	@Test
	void rootTestHello(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/hello", String.class)).isEqualTo("Hello World!");
	}

	@Test
	void rootTestHelloNombre(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/hello?name=Luis", String.class)).isEqualTo("Hello Luis!");
	}

	/*@Test
	void rootTest(@Autowired WebTestClient webClient){
		webClient
				.get().uri("/hello")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Hello World!");
	}*/

}

