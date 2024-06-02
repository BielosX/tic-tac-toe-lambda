package org.ttt.commons;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.UpdateBehavior;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Value
@Builder
@DynamoDbImmutable(builder = Game.GameBuilder.class)
public class Game {
  @Getter(
      onMethod_ = {@DynamoDbPartitionKey, @DynamoDbSecondarySortKey(indexNames = "PlayerIdIndex")})
  String gameId;

  @Getter(onMethod_ = @DynamoDbSecondaryPartitionKey(indexNames = "PlayerIdIndex"))
  String playerId;

  @Getter String opponent;

  ZonedDateTime lastUpdate;

  @Getter(onMethod_ = @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS))
  ZonedDateTime created;
}
