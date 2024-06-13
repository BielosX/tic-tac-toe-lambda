package org.ttt.startgame;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.CreateGameRequest;
import org.ttt.commons.Game;
import org.ttt.commons.GamesService;
import org.ttt.commons.ObjectMapperFactory;

@Slf4j
@SuppressWarnings("unused")
public class StartGame implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
  private static final String PLAYER_ID_HEADER = "playerid";
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
  public APIGatewayV2HTTPResponse handleRequest(
      APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {
    String playerId = apiGatewayV2HTTPEvent.getHeaders().get(PLAYER_ID_HEADER);
    CreateGame request = toCreateGame(apiGatewayV2HTTPEvent.getBody());
    log.info("Creating game for player with ID {}", playerId);
    Game result = gamesService.createNewGame(new CreateGameRequest(playerId, request.opponentId()));
    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withBody(jsonFromGame(result))
        .build();
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
