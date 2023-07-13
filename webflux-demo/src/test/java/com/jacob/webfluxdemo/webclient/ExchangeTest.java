package com.jacob.webfluxdemo.webclient;

import com.jacob.webfluxdemo.dto.InputFailedValidationResponse;
import com.jacob.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ExchangeTest extends BaseTest{
    @Autowired
    private WebClient webClient;

    // Exchange =  Retrieve + additional info http status code

    @Test
    public void stepVerifierExchangeTest() {
        Mono<Object> responseMono = this.webClient.get()
                .uri("reactive-math/square/{num}/throw", 6)
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    private Mono<Object> exchange(ClientResponse cr) {
        if(cr.statusCode() == HttpStatusCode.valueOf(400))
            return cr.bodyToMono(InputFailedValidationResponse.class);
        else
            return cr.bodyToMono(Response.class);
    }
}
