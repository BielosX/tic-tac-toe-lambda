package org.ttt.commons;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

public abstract class SubjectAwareRequestHandler
    implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
  @Override
  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
    return Security.getJwtSubject(input)
        .map(subject -> handleRequestWithSubject(subject, input, context))
        .orElse(
            APIGatewayV2HTTPResponse.builder()
                .withStatusCode(400)
                .withBody("JWT Token should contain subject")
                .build());
  }

  protected abstract APIGatewayV2HTTPResponse handleRequestWithSubject(
      String subject, APIGatewayV2HTTPEvent event, Context context);
}
