package com.shopify.webhook.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.webhook.demo.config.ApiConfig;
import com.shopify.webhook.demo.config.WebClientConfig;
import com.shopify.webhook.demo.model.OrderReturnsRequest;
import com.shopify.webhook.demo.model.OrderReturnsResponse;
import com.shopify.webhook.demo.service.OrderReturnsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class DefaultOrderReturnsService implements OrderReturnsService {

  private final WebClientConfig webClientConfig;
  private final ApiConfig apiConfig;

  public DefaultOrderReturnsService(WebClientConfig webClientConfig, ApiConfig apiConfig) {
    this.webClientConfig = webClientConfig;
    this.apiConfig = apiConfig;
  }

  @Override
  public Mono<OrderReturnsResponse> saveReturnsEvent(OrderReturnsRequest body) {
    log.info("save returns approve event");

    final var webClient = webClientConfig.getHttpWebClient();
    final var uri = apiConfig.getReturnsUri();

    try {
      final var ow = new ObjectMapper().writer();
      final var json = ow.writeValueAsString(body);

      return Mono.just(webClient.method(HttpMethod.POST)
          .uri(uri)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .bodyValue(json)
          .retrieve()
        )
        .flatMap(responseSpec ->
          responseSpec.bodyToMono(OrderReturnsResponse.class)
        );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}