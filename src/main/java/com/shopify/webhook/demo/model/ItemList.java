package com.shopify.webhook.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ItemList {
  @NonNull String sku;
  @NonNull Integer quantity;
}
