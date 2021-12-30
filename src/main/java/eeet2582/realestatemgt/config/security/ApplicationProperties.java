package eeet2582.realestatemgt.config.security;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Value
@ConstructorBinding
@Component
//@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
  private String clientOriginUrl = "http://localhost:4040";
  private String audience = "https://hello-world.example.com";

}
