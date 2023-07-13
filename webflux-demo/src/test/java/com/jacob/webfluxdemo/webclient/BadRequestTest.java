package com.jacob.webfluxdemo.webclient;

import com.jacob.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BadRequestTest extends BaseTest{
    @Autowired
    private WebClient webClient;

    @Test
    public void stepVerifierBadRequestTest() {
        Mono<Response> responseMono = this.webClient.get()
                .uri("reactive-math/square/{num}/throw", 6)
                .retrieve() // Get the response of the GET request
                .bodyToMono(Response.class)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(responseMono)
                .verifyError(WebClientResponseException.BadRequest.class);

    }
}
