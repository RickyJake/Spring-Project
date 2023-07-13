package com.jacob.webfluxdemo.webclient;

import com.jacob.webfluxdemo.dto.MultiplyRequestDto;
import com.jacob.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class HeaderTest extends BaseTest  {
    @Autowired
    private WebClient webClient;

    @Test
    public void stepVerifierHeadersTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .headers(h -> h.set("some-key", "some-value"))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void stepVerifierHeadersBasicAuthTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .headers(h -> h.setBasicAuth("username", "password"))// This is not the good manner. We use directly the WebClientConfig to define credentials
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    private MultiplyRequestDto buildRequestDto(int a, int b) {
        return new MultiplyRequestDto(a, b);
    }
}
