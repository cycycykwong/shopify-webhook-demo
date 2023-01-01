package com.shopify.webhook.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shopify")
@Getter
@Setter
public class ShopifyConfig {

  private String graphqlEndpoint;
  private String accessToken;
  private String secretKey;
}