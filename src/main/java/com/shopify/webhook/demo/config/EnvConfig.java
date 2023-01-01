package com.shopify.webhook.demo.config;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EnvConfig {

  private final Environment env;

  public EnvConfig(Environment env) {
    this.env = env;
  }

  public String getActiveProfile() {
    return env.getActiveProfiles()[0];
  }
}
