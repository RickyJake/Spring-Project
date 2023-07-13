package com.jacob.userservice.service;

import com.jacob.userservice.dto.TransactionRequestDto;
import com.jacob.userservice.dto.TransactionResponseDto;
import com.jacob.userservice.dto.TransactionStatus;
import com.jacob.userservice.entity.UserTransaction;
import com.jacob.userservice.repository.UserRepository;
import com.jacob.userservice.repository.UserTransactionRepository;
import com.jacob.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTransactionRepository userTransactionRepository;

    public Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto requestDto) {
        return this.userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
                .filter(Boolean::booleanValue) // If true the pipe continue, if false it jumps to the defaultIfEmpty
                .map(b -> EntityDtoUtil.toEntity(requestDto))
                .flatMap(this.userTransactionRepository::save)
                .map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
                .defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
    }

    public Flux<UserTransaction> getByUserId(int userId) {
        return this.userTransactionRepository.findByUserId(userId);
    }
}
