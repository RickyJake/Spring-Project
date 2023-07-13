package com.jacob.productservice.service;

import com.jacob.productservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

//@Service
public class DataSetupService implements CommandLineRunner  {
    @Autowired
    private ProductService service;
    @Override
    public void run(String... args) throws Exception {
        ProductDto p1 = new ProductDto("Nokia 3310", 150);
        ProductDto p2 = new ProductDto("4k-TV", 1350);
        ProductDto p3 = new ProductDto("Iphone 10 Pro", 850);
        ProductDto p4 = new ProductDto("Samsung 11", 1400);

       /* Flux.just(p1, p2, p3, p4)
                .concatWith(newProducts())
                .flatMap(p -> this.service.insertProduct(Mono.just(p)))
                .subscribe(System.out::println);*/
    }

    private Flux<ProductDto> newProducts() {
        return Flux.range(1, 1000)
                .delayElements(Duration.ofSeconds(2))
                .map(i -> new ProductDto("product-"+i, ThreadLocalRandom.current().nextInt(10, 1000)));

    }
}
