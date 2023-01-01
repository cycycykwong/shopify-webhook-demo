package com.shopify.webhook.demo.util;

import com.shopify.webhook.demo.config.EnvConfig;
import com.shopify.webhook.demo.constant.EnvironmentProfile;
import com.shopify.webhook.demo.model.ItemList;
import com.shopify.webhook.demo.model.OrderReturnsRequest;
import com.shopify.webhook.demo.model.ShopifyReturnsApproveWebhookResponse;
import com.shopify.webhook.generated.types.Return;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TransformationUtil {
  private final EnvConfig envConfig;

  public TransformationUtil(EnvConfig envConfig) {
    this.envConfig = envConfig;
  }

  /**
   * To map Shopify returns approve event to order return request
   */
  public OrderReturnsRequest toOrderReturnsRequest(
    ShopifyReturnsApproveWebhookResponse webhookData,
    Return returnResponse,
    String domain
  ) {
    final var itemLists = returnResponse.getReturnLineItems().getEdges().stream().map(edge -> {
      final var lineItem = edge.getNode().getFulfillmentLineItem().getLineItem();
      return ItemList.builder()
        .quantity(lineItem.getQuantity())
        .sku(lineItem.getSku())
        .build();
    }).toList();

    final var trackingInfo = returnResponse.getOrder().getFulfillments().get(0).getTrackingInfo().get(0);

    // extract subdomain
    final var subdomain = domainToSubdomain(domain);

    return OrderReturnsRequest.builder()
      .itemList(itemLists)
      .shippingCompanyCode(trackingInfo.getCompany())
      .shippingCompanyName(trackingInfo.getCompany())
      .shippingTrackingNumber(trackingInfo.getNumber())
      .orderId(webhookData.getOrder().getId())
      .returnId(webhookData.getId())
      .returnReason(webhookData.getReturnLineItems().get(0).getReturnReason())
      .subdomain(subdomain)
      .build();
  }

  /**
   * To extract subdomain from Shopify domain
   */
  private String domainToSubdomain(String domain) {
    if (Objects.equals(envConfig.getActiveProfile(), EnvironmentProfile.Local)) {
      return "demo";
    } else {
      return domain.replace(".myshopify.com", "");
    }
  }
}