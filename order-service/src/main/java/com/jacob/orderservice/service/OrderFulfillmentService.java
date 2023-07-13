package com.jacob.orderservice.service;

import com.jacob.orderservice.client.ProductClient;
import com.jacob.orderservice.client.UserClient;
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

import javax.management.monitor.MonitorNotification;
import java.time.Duration;

@Service
public class OrderFulfillmentService {
    @Autowired
    private PurchaseOrderRepository orderRepository;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private UserClient userClient;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
         return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                 .map(EntityDtoUtil::getPurchaseOrder)
                 .map(this.orderRepository::save) // Blocking from JPA
                 .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                 .subscribeOn(Schedulers.boundedElastic()); // Cause of the blocking operation we get a dedicated threadpool

    }

    private Mono<RequestContext> productRequestResponse(RequestContext context) {
        return this.productClient.getProductById(context.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(context::setProductDto)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .thenReturn(context);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext context) {
        return this.userClient.authorizeTransaction(context.getTransactionRequestDto())
                .doOnNext(context::setTransactionResponseDto)
                .thenReturn(context);
    }
}
