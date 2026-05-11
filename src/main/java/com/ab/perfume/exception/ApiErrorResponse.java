package com.ab.perfume.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ApiErrorResponse {

    private Integer status;

    private String message;

    private LocalDateTime timestamp;

    private Map<String, String> errors;

}
