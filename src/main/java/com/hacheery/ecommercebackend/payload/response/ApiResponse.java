package com.hacheery.ecommercebackend.payload.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({
        "success",
        "message"
})
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private static final Long serialize = 1L;
    private boolean success;
    private String message;
    private T data;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
