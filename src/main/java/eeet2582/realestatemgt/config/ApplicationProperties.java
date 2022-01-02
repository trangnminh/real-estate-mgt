package eeet2582.realestatemgt.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  String clientOriginUrl;
  String audience;
}
