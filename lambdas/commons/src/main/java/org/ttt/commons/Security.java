package org.ttt.commons;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import java.util.Optional;

public class Security {
  private static final String SUBJECT_KEY = "sub";

  public static Optional<String> getJwtSubject(APIGatewayV2HTTPEvent event) {
    return Optional.ofNullable(
        event.getRequestContext().getAuthorizer().getJwt().getClaims().get(SUBJECT_KEY));
  }
}
