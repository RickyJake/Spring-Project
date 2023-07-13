package com.jacob.orderservice.client;

import com.jacob.orderservice.dto.TransactionRequestDto;
import com.jacob.orderservice.dto.TransactionResponseDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "users", url = "${user.service.url}")
public interface UserFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "transaction", consumes = "application/json")
    Mono<TransactionResponseDto> authorizeTransaction(@RequestHeader("market-correlation-id") String correlationId, @RequestBody TransactionRequestDto requestDtoMono);
}
