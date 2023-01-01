package com.shopify.webhook.demo.model.shopifyReturnsApproveWebhookResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfillmentLineItem {
  private String id;
  @JsonProperty("admin_graphql_api_id")
  private String adminGraphqlApiId;
  @JsonProperty("line_item")
  private LineItem lineItem;
}
