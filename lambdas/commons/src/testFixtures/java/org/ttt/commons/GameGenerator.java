package org.ttt.commons;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameGenerator {
  private final GamesService gamesService;

  public void generateGames(String playerId, int count) {
    for (int i = 0; i < count; i++) {
      gamesService.createNewGame(new CreateGameRequest(playerId, UUID.randomUUID().toString()));
    }
  }
}
