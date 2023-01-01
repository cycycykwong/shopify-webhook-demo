package com.shopify.webhook.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.webhook.demo.base.BaseResponse;
import com.shopify.webhook.demo.constant.Path;
import com.shopify.webhook.demo.constant.ShopifyHeader;
import com.shopify.webhook.demo.constant.Topic;
import com.shopify.webhook.demo.exception.IncorrectShopifyTopicException;
import com.shopify.webhook.demo.exception.InvalidShopifyHmacHeaderException;
import com.shopify.webhook.demo.exception.MissingHeaderException;
import com.shopify.webhook.demo.model.ShopifyReturnsApproveWebhookResponse;
import com.shopify.webhook.demo.service.OrderReturnsService;
import com.shopify.webhook.demo.service.ShopifyService;
import com.shopify.webhook.demo.util.TransformationUtil;
import com.shopify.webhook.demo.util.VerificationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Log4j2
@RestController
public class ReturnsApproveWebhookController {

  private final ShopifyService shopifyService;
  private final OrderReturnsService orderReturnsService;
  private final VerificationUtil verificationUtil;
  private final TransformationUtil transformationUtil;

  public ReturnsApproveWebhookController(ShopifyService shopifyService, OrderReturnsService orderReturnsService, VerificationUtil verificationUtil, TransformationUtil transformationUtil) {
    this.shopifyService = shopifyService;
    this.orderReturnsService = orderReturnsService;
    this.verificationUtil = verificationUtil;
    this.transformationUtil = transformationUtil;
  }

  /**
   * To transform Shopify returns/approve event response to order returns event
   */
  @PostMapping(Path.RETURNS_APPROVE_WEBHOOK)
  @ResponseBody
  public Mono<ResponseEntity<BaseResponse>> handleReturnsApproveEvent(
    @RequestBody String bodyString,
    HttpServletRequest request
  ) throws Exception {
    log.info("handle Shopify returns/approve event");

    // validate all the required headers
    validateHeaders(request);

    // get Shopify hmac header for verification
    final var hmac = request.getHeader(ShopifyHeader.HMAC);
    // get Shopify subdomain for different shop
    final var domain = request.getHeader(ShopifyHeader.DOMAIN);

    // verify if the hmac headers is valid
    if (!verificationUtil.verifyWebhook(bodyString, hmac)) {
      throw new InvalidShopifyHmacHeaderException();
    }

    // transform raw json body string from http request to ShopifyReturnsApproveWebhookResponse object
    ObjectMapper mapper = new ObjectMapper();
    final var body = mapper.readValue(bodyString, ShopifyReturnsApproveWebhookResponse.class);

    // get return details from Shopify
    // send transformed data to related client backend base on the Shopify domain
    return shopifyService.getReturn(body.getAdminGraphqlApiId())
      .flatMap(returnData -> Mono.just(transformationUtil.toOrderReturnsRequest(body, returnData, domain)))
      .flatMap(orderReturnsService::saveReturnsEvent)
      .flatMap(response -> {
        if (response.getOk()) {
          return Mono.just(HttpStatus.OK).zipWith(Mono.just(response));
        } else {
          return Mono.just(HttpStatus.BAD_REQUEST).zipWith(Mono.just(response));
        }
      })
      .flatMap(tuple2 -> {
        final var httpStatus = tuple2.getT1();
        final var message = tuple2.getT2().getMessage();

        log.info("return event is saved with http status: {} and message: {}", httpStatus.value(), message);

        return Mono.just(BaseResponse.builder()
          .code(httpStatus.value())
          .status(httpStatus)
          .message(message)
          .timestamp(LocalDateTime.now())
          .build());
      })
      .flatMap(baseResponse ->
        Mono.just(
          ResponseEntity
            .status(baseResponse.getStatus())
            .body(baseResponse)
        )
      );
  }

  /**
   * To validate all Shopify headers for returns/approve webhook event
   */
  private void validateHeaders(HttpServletRequest request) {
    final var hmac = request.getHeader(ShopifyHeader.HMAC);
    final var domain = request.getHeader(ShopifyHeader.DOMAIN);
    final var topic = request.getHeader(ShopifyHeader.TOPIC);

    // check if the required headers are present
    if (hmac == null) {
      throw new MissingHeaderException(ShopifyHeader.HMAC);
    }
    if (domain == null) {
      throw new MissingHeaderException(ShopifyHeader.DOMAIN);
    }
    if (topic == null) {
      throw new MissingHeaderException(ShopifyHeader.TOPIC);
    }

    // check if the webhook topic is returns/approve
    if (!topic.equals(Topic.RETURNS_APPROVE)) {
      throw new IncorrectShopifyTopicException();
    }
  }
}