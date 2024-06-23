package org.ttt.commons;

import static org.ttt.commons.GameSymbol.CROSS;
import static org.ttt.commons.GameSymbol.NOUGHT;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstGameSymbolMapper implements GameSymbolMapper {
  @Override
  public Map<String, GameSymbol> mapGameSymbols(String playerId, String opponentId) {
    return Map.of(
        playerId, CROSS,
        opponentId, NOUGHT);
  }
}
