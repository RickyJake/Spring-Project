package com.jacob.orderservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private Integer Id;
    private String name;
    private Integer balance;
}
