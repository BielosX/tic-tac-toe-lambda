package org.ttt.playermove;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.*;
import org.ttt.commons.exceptions.*;

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
    CommitMoveRequest playerMoveRequest = requestFromString(event.getBody());
    String gameId = Optional.ofNullable(event.getPathParameters().get(GAME_ID)).orElseThrow();
    try {
      gamesService.commitMove(gameId, subject, playerMoveRequest);
    } catch (WrongSymbolException e) {
      return fromException(e, 400);
    } catch (GameAlreadyFinishedException
        | PositionAlreadyOccupiedException
        | NotYourTurnException
        | GameRoundDoesNotMatch e) {
      return fromException(e, 409);
    } catch (GameNotFoundException e) {
      return fromException(e, 404);
    }
    return APIGatewayV2HTTPResponse.builder().withStatusCode(204).build();
  }

  @SneakyThrows
  private CommitMoveRequest requestFromString(String requestBody) {
    return objectMapper.readValue(requestBody, CommitMoveRequest.class);
  }

  private APIGatewayV2HTTPResponse fromException(Exception exception, int statusCode) {
    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(statusCode)
        .withBody(exception.getMessage())
        .build();
  }
}
