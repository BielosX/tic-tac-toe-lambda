package org.ttt.commons;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAtomicCounter;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAutoGeneratedTimestampAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.UpdateBehavior;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Value
@Builder
@DynamoDbImmutable(builder = Game.GameBuilder.class)
public class Game {
  private static final String PLAYER_ID_INDEX = "PlayerIdIndex";
  private static final String OPPONENT_ID_INDEX = "OpponentIdIndex";

  @Getter(
      onMethod_ = {
        @DynamoDbPartitionKey,
        @DynamoDbSecondarySortKey(indexNames = {PLAYER_ID_INDEX, OPPONENT_ID_INDEX})
      })
  String gameId;

  @Getter(onMethod_ = @DynamoDbSecondaryPartitionKey(indexNames = PLAYER_ID_INDEX))
  String playerId;

  @Getter(onMethod_ = @DynamoDbSecondaryPartitionKey(indexNames = OPPONENT_ID_INDEX))
  String opponentId;

  @Getter(onMethod_ = @DynamoDbAtomicCounter(startValue = 1))
  Long round;

  @With(onMethod_ = @DynamoDbIgnore)
  GameState state;

  @Getter(onMethod_ = @DynamoDbAutoGeneratedTimestampAttribute)
  Instant lastUpdate;

  @Getter(
      onMethod_ = {
        @DynamoDbAutoGeneratedTimestampAttribute,
        @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS)
      })
  Instant created;

  Map<String, GameSymbol> symbolMapping;

  @With(onMethod_ = @DynamoDbIgnore)
  List<GameMove> moves;
}
