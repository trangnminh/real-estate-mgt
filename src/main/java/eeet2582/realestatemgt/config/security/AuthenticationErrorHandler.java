package eeet2582.realestatemgt.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import eeet2582.realestatemgt.model.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationErrorHandler implements AuthenticationEntryPoint {

  private final ObjectMapper mapper;

  @Override
  public void commence(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final AuthenticationException authException
  ) throws IOException, ServletException {
    final var message = "Unauthorized. %s".formatted(authException.getMessage());
    final var errorMessage = ErrorMessage.from(message);
    final var json = mapper.writeValueAsString(errorMessage);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.flushBuffer();
  }
}
