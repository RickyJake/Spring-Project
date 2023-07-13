package com.jacob.orderservice.service;

import com.jacob.orderservice.dto.PurchaseOrderResponseDto;
import com.jacob.orderservice.entity.PurchaseOrder;
import com.jacob.orderservice.repository.PurchaseOrderRepository;
import com.jacob.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class OrderQueryService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(int userId) {
        return Flux.fromStream(() -> this.purchaseOrderRepository.findByUserId(userId).stream())
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
