package com.jacob.webfluxdemo.webclient;

import com.jacob.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GetSingleResponseTest extends BaseTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void blockTest() {
        Response response = this.webClient.get()
                .uri("reactive-math/square/{num}", 6)
                .retrieve() // Get the response of the GET request
                .bodyToMono(Response.class) //Type of response we are going to get
                .block();//Blocking the response for the test

        System.out.println(response);
    }

    @Test
    public void stepVerifierTest() {
        Mono<Response> responseMono = this.webClient.get()
                .uri("reactive-math/square/{num}", 6)
                .retrieve() // Get the response of the GET request
                .bodyToMono(Response.class); //Type of response we are going to get

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.getOutput() == 36)
                .verifyComplete();

    }
}
