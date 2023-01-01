package com.shopify.webhook.demo.service.impl;

import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.shopify.webhook.demo.config.WebClientConfig;
import com.shopify.webhook.demo.service.ShopifyService;
import com.shopify.webhook.generated.client.ReturnGraphQLQuery;
import com.shopify.webhook.generated.client.ReturnProjectionRoot;
import com.shopify.webhook.generated.types.Return;
import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class DefaultShopifyService implements ShopifyService {

  private final WebClientConfig webClientConfig;

  public DefaultShopifyService(WebClientConfig webClientConfig) {
    this.webClientConfig = webClientConfig;
  }

  @Override
  public Mono<Return> getReturn(String returnId) {
    final var graphQLQueryRequest =
      new GraphQLQueryRequest(
        new ReturnGraphQLQuery.Builder()
          .id(returnId)
          .build(),
        new ReturnProjectionRoot()
          .order()
          .id()
          .fulfillments()
          .trackingInfo()
          .company()
          .number()
          .parent()
          .parent()
          .parent()
          .returnLineItems(10)
          .edges()
          .node()
          .returnReason()
          .fulfillmentLineItem()
          .lineItem()
          .id()
          .name()
          .quantity()
          .sku()
      );
    @Language("graphql") String query = graphQLQueryRequest.serialize();

    try {
      // get graphql web client
      final var graphQLClient = webClientConfig.getGraphqlWebClient();
      final var graphQLResponseMono = graphQLClient.reactiveExecuteQuery(query);
      return graphQLResponseMono.map(r ->
        r.extractValueAsObject("data.return", Return.class)
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}