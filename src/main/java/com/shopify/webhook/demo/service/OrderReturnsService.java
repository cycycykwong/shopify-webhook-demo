package com.shopify.webhook.demo.service;

import com.shopify.webhook.demo.model.OrderReturnsRequest;
import com.shopify.webhook.demo.model.OrderReturnsResponse;
import reactor.core.publisher.Mono;

public interface OrderReturnsService {
  Mono<OrderReturnsResponse> saveReturnsEvent(OrderReturnsRequest body);
}