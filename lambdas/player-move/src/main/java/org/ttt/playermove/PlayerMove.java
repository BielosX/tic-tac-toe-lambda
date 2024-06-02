package org.ttt.playermove;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class PlayerMove implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
  @Override
  public APIGatewayV2HTTPResponse handleRequest(
      APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {
    log.info("Handling APIGatewayV2HTTPEvent");
    log.info("Path params: {}", apiGatewayV2HTTPEvent.getPathParameters());
    return APIGatewayV2HTTPResponse.builder().withBody("Hello World").withStatusCode(200).build();
  }
}
