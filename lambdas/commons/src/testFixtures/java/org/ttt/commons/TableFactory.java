package org.ttt.commons;

import static software.amazon.awssdk.services.dynamodb.model.BillingMode.PAY_PER_REQUEST;
import static software.amazon.awssdk.services.dynamodb.model.KeyType.HASH;
import static software.amazon.awssdk.services.dynamodb.model.KeyType.RANGE;
import static software.amazon.awssdk.services.dynamodb.model.ProjectionType.ALL;

import java.util.List;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

public class TableFactory {
  private static final String GAME_ID = "gameId";
  private static final String PLAYER_ID = "playerId";
  private static final String OPPONENT_ID = "opponentId";

  public static void createGamesTable(DynamoDbClient client, String tableName) {
    client.createTable(
        builder ->
            builder
                .tableName(tableName)
                .billingMode(PAY_PER_REQUEST)
                .keySchema(KeySchemaElement.builder().attributeName(GAME_ID).keyType(HASH).build())
                .globalSecondaryIndexes(
                    List.of(
                        GlobalSecondaryIndex.builder()
                            .indexName("PlayerIdIndex")
                            .projection(p -> p.projectionType(ALL))
                            .keySchema(
                                KeySchemaElement.builder()
                                    .attributeName(PLAYER_ID)
                                    .keyType(HASH)
                                    .build(),
                                KeySchemaElement.builder()
                                    .attributeName(GAME_ID)
                                    .keyType(RANGE)
                                    .build())
                            .build(),
                        GlobalSecondaryIndex.builder()
                            .indexName("OpponentIdIndex")
                            .projection(p -> p.projectionType(ALL))
                            .keySchema(
                                KeySchemaElement.builder()
                                    .attributeName(OPPONENT_ID)
                                    .keyType(HASH)
                                    .build(),
                                KeySchemaElement.builder()
                                    .attributeName(GAME_ID)
                                    .keyType(RANGE)
                                    .build())
                            .build()))
                .attributeDefinitions(
                    AttributeDefinition.builder()
                        .attributeName(GAME_ID)
                        .attributeType(ScalarAttributeType.S)
                        .build(),
                    AttributeDefinition.builder()
                        .attributeName(PLAYER_ID)
                        .attributeType(ScalarAttributeType.S)
                        .build(),
                    AttributeDefinition.builder()
                        .attributeName(OPPONENT_ID)
                        .attributeType(ScalarAttributeType.S)
                        .build()));
  }

  public static void createGamesCountTable(DynamoDbClient client, String tableName) {
    client.createTable(
        builder ->
            builder
                .tableName(tableName)
                .billingMode(PAY_PER_REQUEST)
                .keySchema(
                    KeySchemaElement.builder().attributeName(PLAYER_ID).keyType(HASH).build())
                .attributeDefinitions(
                    AttributeDefinition.builder()
                        .attributeName(PLAYER_ID)
                        .attributeType(ScalarAttributeType.S)
                        .build()));
  }

  public static String getDefaultGamesTableName() {
    return "games-table";
  }

  public static String getDefaultGamesCountTableName() {
    return "games-count-table";
  }

  public static void createAllTables(DynamoDbClient client) {
    createGamesTable(client, getDefaultGamesTableName());
    createGamesCountTable(client, getDefaultGamesCountTableName());
  }
}
