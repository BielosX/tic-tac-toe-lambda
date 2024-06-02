package org.ttt.commons;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.extensions.AutoGeneratedTimestampRecordExtension;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class GamesService {
  private final DynamoDbTable<Game> gamesTable;

  public GamesService() {
    DynamoDbEnhancedClient client =
        DynamoDbEnhancedClient.builder()
            .dynamoDbClient(DynamoDbClient.create())
            .extensions(AutoGeneratedTimestampRecordExtension.create())
            .build();
    String gamesTableName = System.getenv("GAMES_TABLE_NAME");
    this.gamesTable = client.table(gamesTableName, TableSchema.fromClass(Game.class));
  }

  public Optional<Game> getGame(String gameId) {
    return Optional.ofNullable(gamesTable.getItem(Game.builder().gameId(gameId).build()));
  }

  public Game createNewGame(CreateGameRequest request) {
    UUID uuid = UUID.randomUUID();
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    Game game =
        Game.builder()
            .gameId(uuid.toString())
            .playerId(request.hostPlayerId())
            .opponent(request.opponentId())
            .created(now)
            .lastUpdate(now)
            .build();
    gamesTable.putItem(game);
    return game;
  }
}
