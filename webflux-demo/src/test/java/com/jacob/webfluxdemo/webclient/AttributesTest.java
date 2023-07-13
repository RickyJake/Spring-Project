package com.jacob.webfluxdemo.webclient;

import com.jacob.webfluxdemo.dto.MultiplyRequestDto;
import com.jacob.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AttributesTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void stepVerifierHeadersAttributesWithoutSecurityTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void stepVerifierHeadersAttributesBasicTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .attribute("auth", "basic")// We can change the value with other; it'll make token authorization.
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void stepVerifierHeadersAttributesTokenTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .attribute("auth", "other")// We can change the value with other; it'll make token authorization.
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
