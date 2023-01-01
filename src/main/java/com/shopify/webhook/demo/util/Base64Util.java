package com.shopify.webhook.demo.util;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64Util {
  public String encode(byte[] input) {
    Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(input);
  }
}