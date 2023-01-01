package com.shopify.webhook.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class MissingHeaderException extends RuntimeException {
  private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
  private String message;

  public MissingHeaderException(String headerName) {
    this.message = "missing " + headerName + " header";
  }
}