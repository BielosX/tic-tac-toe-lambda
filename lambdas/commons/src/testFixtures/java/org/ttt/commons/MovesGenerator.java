package org.ttt.commons;

import java.util.List;
import java.util.stream.IntStream;

public class MovesGenerator {
  public static void commitMoves(
      GamesService service,
      String gameId,
      String firstPlayerId,
      String secondPlayerId,
      List<List<Integer>> moves) {
    IntStream.range(0, moves.size())
        .forEach(
            i -> {
              String currentPlayerId = i % 2 == 0 ? firstPlayerId : secondPlayerId;
              List<Integer> currentMove = moves.get(i);
              GameSymbol currentSymbol = i % 2 == 0 ? GameSymbol.CROSS : GameSymbol.NOUGHT;
              try {
                service.commitMove(
                    gameId,
                    currentPlayerId,
                    new CommitMoveRequest(
                        i + 1, currentMove.get(0), currentMove.get(1), currentSymbol));
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
  }
}
