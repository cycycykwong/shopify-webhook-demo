package com.shopify.webhook.demo.config;

import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
@Configuration
public class WebClientConfig {

  private final ApiConfig apiConfig;
  private final ShopifyConfig shopifyConfig;

  public WebClientConfig(ApiConfig apiConfig, ShopifyConfig shopifyConfig) {
    this.apiConfig = apiConfig;
    this.shopifyConfig = shopifyConfig;
  }

  public WebClient getHttpWebClient() {
    return WebClient.builder()
      .baseUrl(apiConfig.getBaseUrl())
      .build();
  }

  public WebClientGraphQLClient getGraphqlWebClient() {
    WebClient webClient = WebClient.create(shopifyConfig.getGraphqlEndpoint());
    return MonoGraphQLClient.createWithWebClient(webClient, headers ->
      headers.add("X-Shopify-Access-Token", shopifyConfig.getAccessToken())
    );
  }
}