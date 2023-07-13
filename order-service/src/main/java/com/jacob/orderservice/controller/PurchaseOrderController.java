package com.jacob.orderservice.controller;

import com.jacob.orderservice.dto.OrderStatus;
import com.jacob.orderservice.dto.PurchaseOrderRequestDto;
import com.jacob.orderservice.dto.PurchaseOrderResponseDto;
import com.jacob.orderservice.service.OrderFulfillmentFeignService;
import com.jacob.orderservice.service.OrderQueryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class PurchaseOrderController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Autowired
    private OrderFulfillmentFeignService orderFulfillmentService;
    @Autowired
    private OrderQueryService queryService;

    @PostMapping
    @CircuitBreaker(name = "orderCustomSupportApp", fallbackMethod = "orderFallback")
    public Mono<ResponseEntity<PurchaseOrderResponseDto>> order(@RequestHeader("market-correlation-id") String correlationId, @RequestBody Mono<PurchaseOrderRequestDto> requestDtoMono) {
        logger.info("order() is called");
        return this.orderFulfillmentService.processOrder(correlationId, requestDtoMono)
                .map(ResponseEntity::ok)
                .doOnNext(c -> logger.info("order() has ended"));
                //.onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
                //.onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build() //When the wuebservice is unavailable
                //);
    }

    private Mono<ResponseEntity<PurchaseOrderResponseDto>> orderFallback(@RequestHeader("market-correlation-id") String correlationId, Mono<PurchaseOrderRequestDto> requestDtoMono, Throwable t) {
        logger.error("orderFallback() is called");
        return requestDtoMono.map(this::mapToFailedPurchaseOrder)
                .map(ResponseEntity::ok)
                .doOnNext(c -> logger.error("orderFallback() has ended"));
                //.onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
                //.onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build() //When the wuebservice is unavailable

    }

    @GetMapping("user/{userId}")
    @Timed(value = "getOrdersByUserId.time", description = "Time taken to return Orders for user")
    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(@PathVariable int userId) {
        logger.info("getOrdersByUserId() is called");
        return this.queryService.getOrdersByUserId(userId)
                .doOnNext(c -> logger.info("getOrdersByUserId() has ended"));
    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "helloFallBack")
    public Mono<ResponseEntity<String>> hello() {
        return Mono.just("Welcome to the Order Service")
                .map(ResponseEntity::ok);
    }

    private Mono<ResponseEntity<String>> helloFallBack(Throwable t) {
        return Mono.just("Hi to the Order Fallback Service")
                .map(ResponseEntity::ok);
    }



    private PurchaseOrderResponseDto mapToFailedPurchaseOrder(PurchaseOrderRequestDto requestDto) {
        PurchaseOrderResponseDto dto = new PurchaseOrderResponseDto();
        dto.setProductId(requestDto.getProductId());
        dto.setUserId(requestDto.getUserId());
        dto.setStatus(OrderStatus.FAILED);
        return dto;
    }
}
