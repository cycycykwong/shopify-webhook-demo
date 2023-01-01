package com.shopify.webhook.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnsResponse {
  String code;
  Boolean data;
  String message;
  @JsonProperty("oK")
  Boolean ok;
}