package org.ttt.playermove


import static org.ttt.commons.GameSymbol.CROSS
import static org.ttt.commons.MovesGenerator.commitMoves

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
	String playerId
	String opponentId

	def setup() {
		playerId = UUID.randomUUID().toString()
		opponentId = UUID.randomUUID().toString()
	}

	def setupSpec() {
		localstack.start()
		def dynamoClient = LocalStackDynamoDbClientFactory.create(localstack)
		TableFactory.createAllTables(dynamoClient)
		def parametersProvider = new ConstParametersProvider([
			"GAMES_TABLE_NAME": TableFactory.defaultGamesTableName,
			"GAMES_COUNT_TABLE_NAME": TableFactory.defaultGamesCountTableName,
			"MAX_GAMES_COUNT": "2"
		])
		gamesService = new GamesService(parametersProvider, dynamoClient, new ConstGameSymbolMapper())
		uat = new PlayerMove(gamesService)
	}

	def cleanupSpec() {
		localstack.stop()
	}

	def "should return 404 not found when there is no game with provided ID"() {
		given:
		def gameId = UUID.randomUUID().toString()
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
		def game = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId))
		def gameId = game.gameId
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
		response.statusCode == 204
	}

	def "should return 400 bad request when provided symbol does not match one chosen for player"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
		def body = """
        {
            "round": 1,
            "positionX": 0,
            "positionY": 0,
            "symbol": "NOUGHT"
        }
        """.stripIndent()
		def event = ApiGatewayEventFactory.create(body, playerId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 400
		response.body == "Symbol CROSS expected, received NOUGHT instead"
	}

	def "should save proper move"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
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
		uat.handleRequest(event, null)
		def updatedGame = gamesService.getGame(gameId).get()

		then:
		updatedGame.moves.get(0).x == 0
		updatedGame.moves.get(0).y == 0
		updatedGame.round == 2
		updatedGame.moves.get(0).symbol == CROSS
	}

	def "should return 409 conflict when different round number expected"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
		gamesService.commitMove(gameId, playerId, new CommitMoveRequest(1, 0, 0, CROSS))
		def body = """
        {
            "round": 1,
            "positionX": 0,
            "positionY": 0,
            "symbol": "NOUGHT"
        }
        """.stripIndent()
		def event = ApiGatewayEventFactory.create(body, opponentId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 409
		response.body == "Round number 1 not expected"
	}

	def "should return 409 conflict when other player is on move"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
		gamesService.commitMove(gameId, playerId, new CommitMoveRequest(1, 0, 0, CROSS))
		def body = """
        {
            "round": 2,
            "positionX": 1,
            "positionY": 1,
            "symbol": "CROSS"
        }
		""".stripIndent()
		def event = ApiGatewayEventFactory.create(body, playerId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 409
		response.body == "Not your turn"
	}

	def "should return 409 conflict when position is already occupied"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
		gamesService.commitMove(gameId, playerId, new CommitMoveRequest(1, 0, 0, CROSS))
		def body = """
        {
            "round": 2,
            "positionX": 0,
            "positionY": 0,
            "symbol": "NOUGHT"
        }
		""".stripIndent()
		def event = ApiGatewayEventFactory.create(body, opponentId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 409
		response.body == "Position (0,0) already occupied"
	}

	def "should return 400 bad request when position is out of the game area"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
		def body = """
        {
            "round": 1,
            "positionX": 3,
            "positionY": 3,
            "symbol": "CROSS"
        }
		""".stripIndent()
		def event = ApiGatewayEventFactory.create(body, playerId)
		event.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(event, null)

		then:
		response.statusCode == 400
		response.body == "Position should be in range [0,2]"
	}

	def "should change game state to FINISHED and set winnerId when game is finished"() {
		given:
		def gameId = gamesService.createNewGame(new CreateGameRequest(playerId, opponentId)).gameId
		commitMoves(gamesService, gameId, playerId, opponentId, [
			[0, 0],
			[0, 1],
			[1, 0],
			[1, 1]
		])
		def body = """
        {
            "round": 5,
            "positionX": 2,
            "positionY": 0,
            "symbol": "CROSS"
        }
		""".stripIndent()
		def event = ApiGatewayEventFactory.create(body, playerId)
		event.pathParameters = [gameId: gameId]

		when:
		uat.handleRequest(event, null)
		def game = gamesService.getGame(gameId).get()

		then:
		game.winnerId == playerId
		game.state == GameState.FINISHED
	}
}
