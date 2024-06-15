package org.ttt.describegame;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.Game;
import org.ttt.commons.GamesService;
import org.ttt.commons.ObjectMapperFactory;
import org.ttt.commons.SubjectAwareRequestHandler;

@Slf4j
@SuppressWarnings("unused")
public class DescribeGame extends SubjectAwareRequestHandler {
  private static final String GAME_ID_PARAM = "gameId";
  private final GamesService gamesService = new GamesService();
  private final ObjectMapper objectMapper = ObjectMapperFactory.create();

  protected APIGatewayV2HTTPResponse handleRequestWithSubject(
      String subject, APIGatewayV2HTTPEvent event, Context context) {
    log.info("Subject {} trying to fetch game data", subject);
    String gameId = event.getPathParameters().get(GAME_ID_PARAM);
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
