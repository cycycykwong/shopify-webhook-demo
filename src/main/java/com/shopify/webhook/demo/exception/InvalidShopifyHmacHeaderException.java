package com.shopify.webhook.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidShopifyHmacHeaderException extends RuntimeException {
  private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
  private String message = "invalid hmac header";
}