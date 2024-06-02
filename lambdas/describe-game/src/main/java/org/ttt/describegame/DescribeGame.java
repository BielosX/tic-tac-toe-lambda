package org.ttt.describegame;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.Game;
import org.ttt.commons.GamesService;
import org.ttt.commons.ObjectMapperFactory;

@Slf4j
@SuppressWarnings("unused")
public class DescribeGame
    implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
  private static final String GAME_ID_PARAM = "gameId";
  private final GamesService gamesService = new GamesService();
  private final ObjectMapper objectMapper = ObjectMapperFactory.create();

  @Override
  public APIGatewayV2HTTPResponse handleRequest(
      APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {
    String gameId = apiGatewayV2HTTPEvent.getPathParameters().get(GAME_ID_PARAM);
    log.info("Trying to fetch Game with ID {}", gameId);
    return gamesService
        .getGame(gameId)
        .map(
            game ->
                APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withBody(gameToString(game))
                    .build())
        .orElse(APIGatewayV2HTTPResponse.builder().withStatusCode(404).build());
  }

  @SneakyThrows
  private String gameToString(Game game) {
    return objectMapper.writeValueAsString(game);
  }
}
