package com.hacheery.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private List<String> errors;
}
