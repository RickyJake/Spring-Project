package com.jacob.orderservice.service;

import com.jacob.orderservice.client.ProductFeignClient;
import com.jacob.orderservice.client.UserFeignClient;
import com.jacob.orderservice.dto.PurchaseOrderRequestDto;
import com.jacob.orderservice.dto.PurchaseOrderResponseDto;
import com.jacob.orderservice.dto.RequestContext;
import com.jacob.orderservice.repository.PurchaseOrderRepository;
import com.jacob.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OrderFulfillmentFeignService {
    @Autowired
    private PurchaseOrderRepository orderRepository;
    @Autowired
    private ProductFeignClient productClient;
    @Autowired
    private UserFeignClient userClient;

    public Mono<PurchaseOrderResponseDto> processOrder(String correlationId, Mono<PurchaseOrderRequestDto> requestDtoMono) {
         return requestDtoMono.map(RequestContext::new)
                .flatMap(rc -> this.productRequestResponse(correlationId, rc))
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(c -> this.userRequestResponse(correlationId, c))
                 .map(EntityDtoUtil::getPurchaseOrder)
                 .map(this.orderRepository::save) // Blocking from JPA
                 .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                 .subscribeOn(Schedulers.boundedElastic()); // Cause of the blocking operation we get a dedicated threadpool

    }

    private Mono<RequestContext> productRequestResponse(String correlationId, RequestContext context) {
        return this.productClient.getProductById(correlationId, context.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(context::setProductDto)
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                .thenReturn(context);
    }

    private Mono<RequestContext> userRequestResponse(String correlationId, RequestContext context) {
        return this.userClient.authorizeTransaction(correlationId, context.getTransactionRequestDto())
                .doOnNext(context::setTransactionResponseDto)
                .thenReturn(context);
    }
}
