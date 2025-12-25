package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void healthEndpointWorks() {
        String response = restTemplate.getForObject("/health", String.class);
        assertThat(response).isEqualTo("OK");
    }

    @Test
    void helloEndpointWorks() {
        String response = restTemplate.getForObject("/hello?name=Sunath", String.class);
        assertThat(response).isEqualTo("Hello Sunath");
    }
}
