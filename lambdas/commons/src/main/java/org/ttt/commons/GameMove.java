package org.ttt.commons;

import lombok.Builder;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Value
@Builder
@DynamoDbImmutable(builder = GameMove.GameMoveBuilder.class)
public class GameMove {
  Integer x;
  Integer y;
  GameSymbol symbol;
}
