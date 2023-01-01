package com.shopify.webhook.demo.util;

import com.shopify.webhook.demo.config.ShopifyConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class VerificationUtil {

  private final ShopifyConfig shopifyConfig;
  private final HmacUtil hmacUtil;
  private final Base64Util base64Util;
  private static final String encodingAlgorithm = "HmacSHA256";

  public VerificationUtil(ShopifyConfig shopifyConfig, HmacUtil hmacUtil, Base64Util base64Util) {
    this.shopifyConfig = shopifyConfig;
    this.hmacUtil = hmacUtil;
    this.base64Util = base64Util;
  }

  public Boolean verifyWebhook(String message, String hmac) throws Exception {
    final var secretKey = shopifyConfig.getSecretKey();
    if (secretKey == null) {
      throw new Exception("missing secret key");
    }

    final var calculatedHmac = base64Util.encode(
      hmacUtil.encode(encodingAlgorithm, message, secretKey)
    );
    return hmac.equals(calculatedHmac);
  }
}