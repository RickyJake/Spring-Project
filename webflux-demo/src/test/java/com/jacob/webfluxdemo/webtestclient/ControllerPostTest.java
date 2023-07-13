package com.jacob.webfluxdemo.webtestclient;

import com.jacob.webfluxdemo.controller.ReactiveMathController;
import com.jacob.webfluxdemo.dto.MultiplyRequestDto;
import com.jacob.webfluxdemo.dto.Response;
import com.jacob.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(ReactiveMathController.class)
public class ControllerPostTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    public void postTest() {
        Mockito.when(reactiveMathService.multiply(Mockito.any())).thenReturn(Mono.just(new Response((1)))) ;

        this.client.post()
                .uri("/reactive-math/multiply")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBasicAuth("username", "password"))
                .headers(h -> h.set("somkey", "somevalue"))
                .bodyValue(new MultiplyRequestDto())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
