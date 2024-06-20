package org.ttt.commons;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import java.util.Map;

public class ApiGatewayEventFactory {
  public static APIGatewayV2HTTPEvent create(
      String body, Map<String, String> queryParams, String subject) {
    return APIGatewayV2HTTPEvent.builder()
        .withQueryStringParameters(queryParams)
        .withBody(body)
        .withRequestContext(
            APIGatewayV2HTTPEvent.RequestContext.builder()
                .withAuthorizer(
                    APIGatewayV2HTTPEvent.RequestContext.Authorizer.builder()
                        .withJwt(
                            APIGatewayV2HTTPEvent.RequestContext.Authorizer.JWT
                                .builder()
                                .withClaims(Map.of("sub", subject))
                                .build())
                        .build())
                .build())
        .build();
  }

  public static APIGatewayV2HTTPEvent create(String body, String subject) {
    return create(body, null, subject);
  }
}
