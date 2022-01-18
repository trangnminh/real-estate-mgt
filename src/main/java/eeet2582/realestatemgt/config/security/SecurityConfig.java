package eeet2582.realestatemgt.config.security;

import eeet2582.realestatemgt.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationErrorHandler authenticationErrorHandler;

    private final OAuth2ResourceServerProperties resourceServerProps;

    private final ApplicationProperties applicationProps;

    @Override
    protected void configure(final @NotNull HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .oauth2ResourceServer()
                .authenticationEntryPoint(authenticationErrorHandler)
                .jwt()
                .decoder(makeJwtDecoder())
                .jwtAuthenticationConverter(makePermissionsConverter());

        http.authorizeRequests()
                // PUBLIC API
                .antMatchers(HttpMethod.GET, "/api/v1/houses/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/houses/search/form").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/meetings").permitAll()
                // PRIVATE API
                .anyRequest().authenticated();

//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/api/v1/users/**").authenticated()
//                .antMatchers(HttpMethod.POST, "/api/v1/users").authenticated()
//                .antMatchers(HttpMethod.PUT, "/api/v1/users").authenticated()
//                .antMatchers(HttpMethod.DELETE, "/api/v1/users/**").authenticated()
//                .antMatchers(HttpMethod.POST, "/api/v1/houses").authenticated()
//                .antMatchers(HttpMethod.PUT, "/api/v1/houses/**").authenticated()
//                .antMatchers(HttpMethod.DELETE, "/api/v1/houses").authenticated()
//                .antMatchers("/api/v1/deposits/**").authenticated()
//                .antMatchers("/api/v1/meetings/**").authenticated()
//                .antMatchers("/api/v1/rentals/**").authenticated()
//                .antMatchers("/api/v1/payments/**").authenticated()
//                .and()
//                .cors()
//                .and()
//                .csrf().disable()
//                .oauth2ResourceServer()
//                .authenticationEntryPoint(authenticationErrorHandler)
//                .jwt()
//                .decoder(makeJwtDecoder())
//                .jwtAuthenticationConverter(makePermissionsConverter());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name()
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
        return source;
    }

    private @NotNull JwtDecoder makeJwtDecoder() {
        final var issuer = resourceServerProps.getJwt().getIssuerUri();
        final var decoder = JwtDecoders.<NimbusJwtDecoder>fromIssuerLocation(issuer);
        final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        final var tokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, this::withAudience);
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
