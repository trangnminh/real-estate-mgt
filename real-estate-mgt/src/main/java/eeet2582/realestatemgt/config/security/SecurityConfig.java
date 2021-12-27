package eeet2582.realestatemgt.config.security;

import eeet2582.realestatemgt.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationErrorHandler authenticationErrorHandler;

  private final OAuth2ResourceServerProperties resourceServerProps;

  private final ApplicationProperties applicationProps;

  @Override
  public void configure(final @NotNull WebSecurity web) throws Exception {
    final var exclusionRegex = "^(?!%s|%s).*$".formatted(
            "/api/messages/protected",
            "/api/messages/admin"
    );

    web.ignoring()
            .regexMatchers(exclusionRegex);
  }

  @Override
  protected void configure(final @NotNull HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/api/messages/protected", "/api/messages/admin")
            .authenticated()
            .anyRequest()
            .permitAll()
            .and()
            .cors()
            .and()
            .oauth2ResourceServer()
            .authenticationEntryPoint(authenticationErrorHandler)
            .jwt()
            .decoder(makeJwtDecoder())
            .jwtAuthenticationConverter(makePermissionsConverter());
  }

  private @NotNull JwtDecoder makeJwtDecoder() {
    final var issuer = resourceServerProps.getJwt().getIssuerUri();
    final var decoder = JwtDecoders.<NimbusJwtDecoder>fromIssuerLocation(issuer);
    final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    final var tokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, this::withAudience);
    System.out.println("issuer " + issuer);
    System.out.println("decoder " + decoder);
    System.out.println("withIssuer " + withIssuer);
    System.out.println("tokenValidator " + tokenValidator);
    decoder.setJwtValidator(tokenValidator);
    return decoder;
  }

  private OAuth2TokenValidatorResult withAudience(final @NotNull Jwt token) {
    final var audienceError = new OAuth2Error(
            OAuth2ErrorCodes.INVALID_TOKEN,
            "The token was not issued for the given audience",
            "https://datatracker.ietf.org/doc/html/rfc6750#section-3.1"
    );

    return token.getAudience().contains(applicationProps.getAudience())
            ? OAuth2TokenValidatorResult.success()
            : OAuth2TokenValidatorResult.failure(audienceError);
  }

  private @NotNull JwtAuthenticationConverter makePermissionsConverter() {
    final var jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtAuthoritiesConverter.setAuthoritiesClaimName("permissions");
    jwtAuthoritiesConverter.setAuthorityPrefix("");

    final var jwtAuthConverter = new JwtAuthenticationConverter();
    jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);

    return jwtAuthConverter;
  }
}
