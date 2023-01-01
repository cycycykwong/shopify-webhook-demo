package com.shopify.webhook.demo.exception;

import com.shopify.webhook.demo.base.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * To handle incorrect shopify topic header exception
   */
  @ExceptionHandler({IncorrectShopifyTopicException.class})
  protected ResponseEntity<BaseResponse> handleError(
    final HttpServletRequest request,
    final IncorrectShopifyTopicException exception
  ) {
    log.error("error = ", exception);

    final var httpStatus = exception.getHttpStatus();
    final var body = BaseResponse.builder()
      .code(httpStatus.value())
      .status(httpStatus)
      .message(exception.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return ResponseEntity.status(httpStatus).body(body);
  }

  /**
   * To handle missing shopify header exception
   */
  @ExceptionHandler({MissingHeaderException.class})
  protected ResponseEntity<BaseResponse> handleError(
    final HttpServletRequest request,
    final MissingHeaderException exception
  ) {
    log.error("error = ", exception);

    final var httpStatus = exception.getHttpStatus();
    final var body = BaseResponse.builder()
      .code(httpStatus.value())
      .status(httpStatus)
      .message(exception.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return ResponseEntity.status(httpStatus).body(body);
  }

  /**
   * To handle invalid shopify hmac header
   */
  @ExceptionHandler({InvalidShopifyHmacHeaderException.class})
  protected ResponseEntity<BaseResponse> handleError(
    final HttpServletRequest request,
    final InvalidShopifyHmacHeaderException exception
  ) {
    log.error("error = ", exception);

    final var httpStatus = exception.getHttpStatus();
    final var body = BaseResponse.builder()
      .code(httpStatus.value())
      .status(httpStatus)
      .message(exception.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return ResponseEntity.status(httpStatus).body(body);
  }

  /**
   * To handle all unexpected errors
   */
  @ExceptionHandler({Exception.class})
  protected ResponseEntity<BaseResponse> handleUnexpectedError(
    final HttpServletRequest request,
    final Exception exception
  ) {
    log.error(this + " : unexpected error = ", exception);

    final var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    final var body = BaseResponse.builder()
      .code(httpStatus.value())
      .status(httpStatus)
      .message(exception.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return ResponseEntity.status(httpStatus).body(body);
  }
}