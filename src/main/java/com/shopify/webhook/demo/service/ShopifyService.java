package com.shopify.webhook.demo.service;

import com.shopify.webhook.generated.types.Return;
import reactor.core.publisher.Mono;

public interface ShopifyService {
  Mono<Return> getReturn(String returnId);
}