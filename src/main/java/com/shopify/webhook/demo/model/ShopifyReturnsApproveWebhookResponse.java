package com.shopify.webhook.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopify.webhook.demo.model.shopifyReturnsApproveWebhookResponse.Order;
import com.shopify.webhook.demo.model.shopifyReturnsApproveWebhookResponse.ReturnLineItem;
import lombok.*;

import java.util.List;

/**
 * Shopify returns/approve webhook response, please refer to
 * https://shopify.dev/api/admin-graphql/2023-01/objects/Return for more details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopifyReturnsApproveWebhookResponse {
  private String id;
  @JsonProperty("admin_graphql_api_id")
  private String adminGraphqlApiId;
  @JsonProperty("status")
  private String status;
  @JsonProperty("order")
  private Order order;
  @JsonProperty("total_return_line_items")
  private String totalReturnLineItems;
  @JsonProperty("name")
  private String name;
  @JsonProperty("return_line_items")
  private List<ReturnLineItem> returnLineItems;
}