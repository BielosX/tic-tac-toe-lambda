package org.ttt.playermove


import org.ttt.commons.*
import spock.lang.Shared
import spock.lang.Specification

class PlayerMoveSpec extends Specification {
	@Shared
	def localstack = new DynamoDbLocalStackContainer()
	@Shared
	GamesService gamesService
	@Shared
	PlayerMove uat

	def setupSpec() {
		localstack.start()
		def dynamoClient = LocalStackDynamoDbClientFactory.create(localstack)
		TableFactory.createAllTables(dynamoClient)
		def parametersProvider = new ConstParametersProvider([
			"GAMES_TABLE_NAME": TableFactory.defaultGamesTableName,
			"GAMES_COUNT_TABLE_NAME": TableFactory.defaultGamesCountTableName,
			"MAX_GAMES_COUNT": "2"
		])
		gamesService = new GamesService(parametersProvider, dynamoClient)
		uat = new PlayerMove(gamesService)
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
		response.body == "Game with ID ${gameId} not found"
	}

	def "should return 204 no content on success"() {
		given:
		def hostPlayerId = UUID.randomUUID().toString()
		def opponentId = UUID.randomUUID().toString()
		def game = gamesService.createNewGame(new CreateGameRequest(hostPlayerId, opponentId))
		def gameId = game.gameId
		def symbol = game.symbolMapping[hostPlayerId]
		def body = """
        {
            "round": 1,
            "positionX": 0,
            "positionY": 0,
            "symbol": "${symbol}"
        }
        """.stripIndent()
		def event = ApiGatewayEventFactory.create(body, hostPlayerId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 204
	}

	def "should return 400 bad request when provided symbol does not match one chosen for player"() {
		given:
		def hostPlayerId = UUID.randomUUID().toString()
		def opponentId = UUID.randomUUID().toString()
		def gameId = gamesService.createNewGame(new CreateGameRequest(hostPlayerId, opponentId)).gameId
		def game = gamesService.getGame(gameId).get()
		def opponentSymbol = game.symbolMapping[opponentId]
		def playerSymbol = game.symbolMapping[hostPlayerId]
		def body = """
        {
            "round": 1,
            "positionX": 0,
            "positionY": 0,
            "symbol": "${opponentSymbol}"
        }
        """.stripIndent()
		def event = ApiGatewayEventFactory.create(body, hostPlayerId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 400
		response.body == "Symbol ${playerSymbol} expected, received ${opponentSymbol} instead"
	}
}
