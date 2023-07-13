package com.jacob.orderservice.util;


import com.jacob.orderservice.dto.*;
import com.jacob.orderservice.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static void setTransactionRequestDto(RequestContext requestContext){
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        dto.setAmount(requestContext.getProductDto().getPrice());
        requestContext.setTransactionRequestDto(dto);
    }

    public static PurchaseOrder getPurchaseOrder(RequestContext context) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(context.getPurchaseOrderRequestDto().getUserId());
        purchaseOrder.setProductId(context.getPurchaseOrderRequestDto().getProductId());
        purchaseOrder.setAmount(context.getProductDto().getPrice());

        TransactionStatus status = context.getTransactionResponseDto().getStatus();
        OrderStatus orderStatus = TransactionStatus.APPROVED.equals(status) ? OrderStatus.COMPLETED : OrderStatus.FAILED;
        purchaseOrder.setStatus(orderStatus);

        return purchaseOrder;
    }

    public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDto purchaseOrderResponseDto = new PurchaseOrderResponseDto();
        BeanUtils.copyProperties(purchaseOrder, purchaseOrderResponseDto);
        purchaseOrderResponseDto.setOrderId(purchaseOrder.getId());
        return purchaseOrderResponseDto;
    }



}
