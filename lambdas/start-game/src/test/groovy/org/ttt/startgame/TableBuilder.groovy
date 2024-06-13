package org.ttt.startgame

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.BillingMode
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

class TableBuilder {
	def static PLAYER_ID = "playerId"
	def static GAME_ID = "gameId"

	static void createGamesTable(DynamoDbClient client, String name) {
		client.createTable({builder ->
			builder
					.tableName(name)
					.billingMode(BillingMode.PAY_PER_REQUEST)
					.keySchema({ b ->
						b.attributeName(GAME_ID).keyType(KeyType.HASH)
					}
					)
					.attributeDefinitions({ b ->
						b.attributeName(GAME_ID)
								.attributeType(ScalarAttributeType.S).build()
					})
					.build()
		})
	}

	static void createGamesCountTable(DynamoDbClient client, String name) {
		client.createTable({ builder ->
			builder
					.tableName(name)
					.billingMode(BillingMode.PAY_PER_REQUEST)
					.keySchema({ b -> b.attributeName(PLAYER_ID).keyType(KeyType.HASH)})
					.attributeDefinitions({ b -> b.attributeName(PLAYER_ID).attributeType(ScalarAttributeType.S).build() })
					.build()
		})
	}
}
