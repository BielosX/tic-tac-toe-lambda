package org.ttt.commons

import static org.ttt.commons.GameSymbol.NOUGHT

import org.ttt.commons.exceptions.GameAlreadyFinishedException
import org.ttt.commons.exceptions.WrongSymbolException
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import spock.lang.Shared
import spock.lang.Specification

class GamesServiceSpec extends Specification {
	@Shared
	ParametersProvider provider =  new ConstParametersProvider([
		"GAMES_TABLE_NAME": TableFactory.defaultGamesTableName,
		"GAMES_COUNT_TABLE_NAME": TableFactory.defaultGamesCountTableName,
		"MAX_GAMES_COUNT": "2"
	])

	def "should throw exception when game is already finished"() {
		setup:
		def client = Mock(DynamoDbClient)
		GamesService uat = new GamesService(provider, client)
		def playerId = UUID.randomUUID().toString()
		def gameId = UUID.randomUUID().toString()
		client.getItem(_) >> GetItemResponse.builder()
				.item([
					state: AttributeValue.fromS("FINISHED")
				]).build()
		when:
		uat.commitMove(playerId, gameId, null)

		then:
		thrown GameAlreadyFinishedException
	}

	def "should throw exception when wrong symbol is provided"() {
		setup:
		def client = Mock(DynamoDbClient)
		GamesService uat = new GamesService(provider, client)
		def playerId = UUID.randomUUID().toString()
		def opponentId = UUID.randomUUID().toString()
		def gameId = UUID.randomUUID().toString()
		client.getItem(_) >> GetItemResponse.builder()
				.item([
					"state": AttributeValue.fromS("ACTIVE"),
					"symbolMapping": AttributeValue.fromM([
						(playerId): AttributeValue.fromS("CROSS"),
						(opponentId): AttributeValue.fromS("NOUGHT"),
					])
				]).build()
		when:
		uat.commitMove(playerId, gameId, new CommitMoveRequest(5, 0, 0, NOUGHT))

		then:
		thrown WrongSymbolException
	}
}
