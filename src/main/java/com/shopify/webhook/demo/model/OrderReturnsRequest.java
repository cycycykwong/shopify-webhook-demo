package com.shopify.webhook.demo.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderReturnsRequest {
  String subdomain;
  String returnId;
  String orderId;
  String shippingCompanyCode;
  String shippingCompanyName;
  String shippingTrackingNumber;

  String returnReason;
  List<ItemList> itemList;

}