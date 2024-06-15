package org.ttt.startgame;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.*;
import org.ttt.commons.exceptions.TooManyActiveGamesException;

@Slf4j
@SuppressWarnings("unused")
public class StartGame extends SubjectAwareRequestHandler {
  private final ObjectMapper mapper = ObjectMapperFactory.create();
  private final GamesService gamesService;

  public StartGame() {
    this.gamesService = new GamesService();
  }

  public StartGame(GamesService gamesService) {
    this.gamesService = gamesService;
  }

  public record CreateGame(String opponentId) {}

  @Override
  protected APIGatewayV2HTTPResponse handleRequestWithSubject(
      String subject, APIGatewayV2HTTPEvent event, Context context) {
    CreateGame request = toCreateGame(event.getBody());
    log.info("Creating game for player with ID {}", subject);
    try {
      Game result =
          gamesService.createNewGame(new CreateGameRequest(subject, request.opponentId()));
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(jsonFromGame(result))
          .build();
    } catch (TooManyActiveGamesException exception) {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(409)
          .withBody(exception.getMessage())
          .build();
    }
  }

  @SneakyThrows
  private CreateGame toCreateGame(String body) {
    return mapper.readValue(body, CreateGame.class);
  }

  @SneakyThrows
  private String jsonFromGame(Game game) {
    return mapper.writeValueAsString(game);
  }
}
