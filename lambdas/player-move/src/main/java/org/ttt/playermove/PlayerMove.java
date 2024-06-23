package org.ttt.playermove;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.*;
import org.ttt.commons.exceptions.GameAlreadyFinishedException;
import org.ttt.commons.exceptions.GameNotFoundException;
import org.ttt.commons.exceptions.WrongSymbolException;

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
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(400)
          .withBody(e.getMessage())
          .build();
    } catch (GameAlreadyFinishedException e) {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(409)
          .withBody(e.getMessage())
          .build();
    } catch (GameNotFoundException e) {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(404)
          .withBody(e.getMessage())
          .build();
    }
    return APIGatewayV2HTTPResponse.builder().withStatusCode(204).build();
  }

  @SneakyThrows
  private CommitMoveRequest requestFromString(String requestBody) {
    return objectMapper.readValue(requestBody, CommitMoveRequest.class);
  }
}
