package com.shopify.webhook.demo.base;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BaseResponse {
  private Integer code;
  private HttpStatus status;
  private String message;
  private LocalDateTime timestamp;
}