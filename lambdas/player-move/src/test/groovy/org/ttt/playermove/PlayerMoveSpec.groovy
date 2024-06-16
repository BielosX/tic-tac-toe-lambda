package org.ttt.playermove


import org.ttt.commons.*
import spock.lang.Shared
import spock.lang.Specification

class PlayerMoveSpec extends Specification {
	@Shared
	def localstack = new DynamoDbLocalStackContainer()
	@Shared
	PlayerMove uat

	def setupSpec() {
		localstack.start()
		def dynamoClient = LocalStackDynamoDbClientFactory.create(localstack)
		TableFactory.createAllTables(dynamoClient)
		def parametersProvider = new ConstParametersProvider()
		parametersProvider.setParameter("GAMES_TABLE_NAME", TableFactory.defaultGamesTableName)
		parametersProvider.setParameter("GAMES_COUNT_TABLE_NAME", TableFactory.defaultGamesCountTableName)
		parametersProvider.setParameter("MAX_GAMES_COUNT", "2")
		def gameService = new GamesService(parametersProvider, dynamoClient)
		uat = new PlayerMove(gameService)
	}

	def cleanupSpec() {
		localstack.stop()
	}

	def "should return 404 not found when there is no game with provided ID"() {
		given:
		def gameId = UUID.randomUUID().toString()
		def playerId = UUID.randomUUID().toString()
		def body = """
        {
            "round": 1,
            "positionX": 0,
            "positionY": 0,
            "symbol": "CROSS"
        }
        """.stripIndent()
		def event = ApiGatewayEventFactory.create(body, playerId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 404
		response.body == "Game not found"
	}
}
