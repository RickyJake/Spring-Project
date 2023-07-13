package com.jacob.webfluxdemo.webclient;


import com.jacob.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class GetMultiResponseTest extends BaseTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void stepVerifierFluxTest() {
        Flux<Response> responseFlux = this.webClient.get()
                .uri("reactive-math/table/{num}", 6)
                .retrieve() // Get the response of the GET request
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();

    }

    @Test
    public void stepVerifierFluxStreamTest() {
        Flux<Response> responseFlux = this.webClient.get()
                .uri("reactive-math/table/{num}/stream", 6)
                .retrieve() // Get the response of the GET request
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();

    }
}
