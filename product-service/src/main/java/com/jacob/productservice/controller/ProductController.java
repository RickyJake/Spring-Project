package com.jacob.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jacob.productservice.config.ProductsServiceConfig;
import com.jacob.productservice.dto.ProductDto;
import com.jacob.productservice.dto.Properties;
import com.jacob.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @Autowired
    private ProductsServiceConfig productConfig;

    @GetMapping("properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(productConfig.getMsg(), productConfig.getBuildVersion(),
                productConfig.getMailDetails(), productConfig.getActiveBranches());
        return ow.writeValueAsString(properties);
    }

    @GetMapping("all")
    public Flux<ProductDto> all() {
        return this.service.getAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@RequestHeader("market-correlation-id") String correlationId, @PathVariable String id) {
        logger.info("getProductById() is called");
        this.simulateRandomException();
        return this.service.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnNext(c -> logger.info("getProductById() has ended"));
    }

    @GetMapping("price-range")
    public Flux<ProductDto> getProductByPriceRange(@RequestParam("min") int min,
                                                                   @RequestParam("max") int max){
        return this.service.getProductsByPriceRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> insertProduct(@RequestHeader("market-correlation-id") String correlationId, @RequestBody Mono<ProductDto> productDtoMono) {
        return this.service.insertProduct(productDtoMono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id, @RequestBody Mono<ProductDto> productDtoMono) {
        return this.service.updateProduct(id, productDtoMono)
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return this.service.deleteProduct(id);
    }

    private void simulateRandomException() {
        int nextInt = ThreadLocalRandom.current().nextInt(1, 10);

        if (nextInt > 2)
            throw new RuntimeException("Something is wrong");
    }
}
