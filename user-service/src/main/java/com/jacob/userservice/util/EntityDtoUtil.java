package com.jacob.userservice.util;

import com.jacob.userservice.dto.TransactionRequestDto;
import com.jacob.userservice.dto.TransactionResponseDto;
import com.jacob.userservice.dto.TransactionStatus;
import com.jacob.userservice.dto.UserDto;
import com.jacob.userservice.entity.User;
import com.jacob.userservice.entity.UserTransaction;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

public class EntityDtoUtil {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        return user;
    }

    public static UserTransaction toEntity(TransactionRequestDto requestDto) {
        UserTransaction transaction = new UserTransaction();
        transaction.setUserId(requestDto.getUserId());
        transaction.setAmount(requestDto.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    public static TransactionResponseDto toDto(TransactionRequestDto requestDto, TransactionStatus status) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setAmount(requestDto.getAmount());
        dto.setUserId(requestDto.getUserId());
        dto.setStatus(status);

        return dto;
    }
}
