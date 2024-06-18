package org.ttt.playermove;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.*;

@Slf4j
@SuppressWarnings("unused")
public class PlayerMove extends SubjectAwareRequestHandler {
  private static final String GAME_ID = "gameId";
  private final ObjectMapper objectMapper = ObjectMapperFactory.create();
  private final GamesService gamesService;

  public PlayerMove(GamesService gamesService) {
    this.gamesService = gamesService;
  }

  public PlayerMove() {
    this.gamesService = new GamesService();
  }

  @Override
  protected APIGatewayV2HTTPResponse handleRequestWithSubject(
      String subject, APIGatewayV2HTTPEvent event, Context context) {
    log.debug("Received path params: {}", event.getPathParameters());
    PlayerMoveRequest playerMoveRequest = requestFromString(event.getBody());
    return Optional.ofNullable(event.getPathParameters().get(GAME_ID))
        .flatMap(gamesService::getGame)
        .map(game -> processMove(subject, playerMoveRequest, game))
        .orElse(
            APIGatewayV2HTTPResponse.builder()
                .withStatusCode(404)
                .withBody("Game not found")
                .build());
  }

  public record PlayerMoveRequest(
      Integer round, Integer positionX, Integer positionY, GameSymbol symbol) {}

  @SneakyThrows
  private PlayerMoveRequest requestFromString(String requestBody) {
    return objectMapper.readValue(requestBody, PlayerMoveRequest.class);
  }

  private APIGatewayV2HTTPResponse processMove(
      String subject, PlayerMoveRequest playerMoveRequest, Game game) {
    GameSymbol expectedSymbol = game.getSymbolMapping().get(subject);
    if (playerMoveRequest.symbol() != expectedSymbol) {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(400)
          .withBody(String.format("Wrong symbol specified. Your symbol is %s", expectedSymbol))
          .build();
    }
    return APIGatewayV2HTTPResponse.builder().withStatusCode(204).build();
  }
}
