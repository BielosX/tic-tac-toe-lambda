package org.ttt.commons;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class GameGenerator {
  private final GamesService gamesService;

  @SneakyThrows
  public void generateGames(String playerId, int count) {
    for (int i = 0; i < count; i++) {
      gamesService.createNewGame(new CreateGameRequest(playerId, UUID.randomUUID().toString()));
    }
  }
}
