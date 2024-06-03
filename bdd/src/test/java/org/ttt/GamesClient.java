package org.ttt;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.Optional;

public interface GamesClient {
  @RequestLine("GET /games/{gameId}")
  GetGameResponse getGameById(@Param("gameId") String gameId);

  @RequestLine("POST /games")
  @Headers({"PlayerId: {playerId}", "Content-Type: application/json"})
  CreateGameResponse createNewGame(
      CreateGameRequest createGameRequest, @Param("playerId") String playerId);
}
