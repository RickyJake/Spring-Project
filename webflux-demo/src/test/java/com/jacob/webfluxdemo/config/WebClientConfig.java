package com.jacob.webfluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                //.defaultHeaders(h -> h.setBasicAuth("username", "password")); Sometimes we cannot set credentials like this, but dynamically => we use ExchangeFilterFunction
                .filter(this::sessionToken)
                .build();
    }

    /*private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
        System.out.println("Generating Session Token");
        // As we can't change the ClientRequest cause of the immutability; we clone it and change it
        ClientRequest clientRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth("some-jwt-token")).build();
        return ex.exchange(clientRequest);
    }*/

    // Change method of authentification dynamically
    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
        // key auth -> values basic or oauth
        ClientRequest clientRequest = request.attribute("auth")
                .map(v -> v.equals("basic") ? withBasicAuth(request) : withOAuth(request))
                .orElse(request);
        return ex.exchange(clientRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(h -> h.setBasicAuth("username", "password"))
                .build();
    }

    private ClientRequest withOAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("some-token"))
                .build();
    }
}
