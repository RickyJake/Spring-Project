package com.jacob.userservice.controller;

import com.jacob.userservice.dto.TransactionRequestDto;
import com.jacob.userservice.dto.TransactionResponseDto;
import com.jacob.userservice.entity.UserTransaction;
import com.jacob.userservice.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user/transaction")
public class UserTransactionController {
    private static final Logger logger = LoggerFactory.getLogger(UserTransactionController.class);
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public Flux<UserTransaction> getByUserId(@RequestHeader("market-correlation-id") String correlationId, @RequestParam("userId") int id) {
        return this.transactionService.getByUserId(id);
    }

    @PostMapping
    public Mono<TransactionResponseDto> createTransaction(@RequestHeader("market-correlation-id") String correlationId, @RequestBody Mono<TransactionRequestDto> requestDtoMono) {
        logger.info("createTransaction() is called");
        return requestDtoMono.flatMap(this.transactionService::createTransaction)
                .doOnNext(c -> logger.info("createTransaction() has ended"));
    }
}
