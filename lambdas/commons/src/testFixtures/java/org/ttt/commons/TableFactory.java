package org.ttt.commons;

import static software.amazon.awssdk.services.dynamodb.model.BillingMode.PAY_PER_REQUEST;
import static software.amazon.awssdk.services.dynamodb.model.KeyType.HASH;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class TableFactory {
  private static final String GAME_ID = "gameId";
  private static final String PLAYER_ID = "playerId";

  public static void createGamesTable(DynamoDbClient client, String tableName) {
    client.createTable(
        builder ->
            builder
                .tableName(tableName)
                .billingMode(PAY_PER_REQUEST)
                .keySchema(KeySchemaElement.builder().attributeName(GAME_ID).keyType(HASH).build())
                .attributeDefinitions(
                    AttributeDefinition.builder()
                        .attributeName(GAME_ID)
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
}
