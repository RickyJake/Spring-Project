package com.jacob.webfluxdemo.webtestclient;

import com.jacob.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class SimpleWebTestClient {
    @Autowired
    private WebTestClient webClient;

    @Test
    public void blockTest() {
        Flux<Response> response = this.webClient.get()
                .uri("/reactive-math/square/{num}", 6)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Response.class)
                .getResponseBody();

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getOutput() == 36)
                .verifyComplete();
    }

    @Test
    public void fluentAssertionTest() {
        this.webClient.get()
                .uri("/reactive-math/square/{num}", 6)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(36));


    }
}
