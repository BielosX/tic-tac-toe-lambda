package org.ttt.commons;

import java.util.Map;

public interface GameSymbolMapper {
  Map<String, GameSymbol> mapGameSymbols(String playerId, String opponentId);
}
