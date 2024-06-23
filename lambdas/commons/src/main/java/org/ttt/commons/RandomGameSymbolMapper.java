package org.ttt.commons;

import java.util.Map;
import java.util.Random;

public class RandomGameSymbolMapper implements GameSymbolMapper {
  private final Random random = new Random();

  @Override
  public Map<String, GameSymbol> mapGameSymbols(String playerId, String opponentId) {
    int playerSymbolIndex = random.nextInt(0, 2);
    int opponentSymbolIndex = (playerSymbolIndex + 1) % 2;
    GameSymbol[] values = GameSymbol.values();
    return Map.of(
        playerId, values[playerSymbolIndex],
        opponentId, values[opponentSymbolIndex]);
  }
}
