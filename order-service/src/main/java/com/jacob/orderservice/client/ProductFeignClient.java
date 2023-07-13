package com.jacob.orderservice.client;

import com.jacob.orderservice.dto.ProductDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "products", url = "${product.service.url}")
public interface ProductFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "{id}", consumes = "application/json")
    Mono<ProductDto> getProductById(@RequestHeader("market-correlation-id") String correlationId, @PathVariable("id") String id);
}
