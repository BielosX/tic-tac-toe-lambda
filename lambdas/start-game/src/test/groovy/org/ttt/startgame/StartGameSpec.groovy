package org.ttt.startgame

import groovy.json.JsonSlurper
import java.util.stream.Stream
import org.ttt.commons.*
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import spock.lang.Shared
import spock.lang.Specification

class StartGameSpec extends Specification {
	@Shared
	def localstack = new DynamoDbLocalStackContainer()
	@Shared
	def parser = new JsonSlurper()
	@Shared
	GameGenerator generator
	@Shared
	StartGame uat

	def setupSpec() {
		localstack.start()
		def dynamoClient = LocalStackDynamoDbClientFactory.create(localstack)
		TableFactory.createAllTables(dynamoClient)
		def parametersProvider = new ConstParametersProvider([
			"GAMES_TABLE_NAME": TableFactory.defaultGamesTableName,
			"GAMES_COUNT_TABLE_NAME": TableFactory.defaultGamesCountTableName,
			"MAX_GAMES_COUNT": "2"
		])
		def gameService = new GamesService(parametersProvider, dynamoClient, new ConstGameSymbolMapper())
		generator = new GameGenerator(gameService)
		uat = new StartGame(gameService)
	}

	def cleanupSpec() {
		localstack.stop()
	}

	def "should return newly created game"() {
		given:
		def hostPlayerId = UUID.randomUUID().toString()
		def opponentId = UUID.randomUUID().toString()
		def body = """
		{
			"opponentId": "$opponentId"
		}
		""".stripIndent()
		def request = ApiGatewayEventFactory.create(body, hostPlayerId)

		when:
		def response = uat.handleRequest(request, null)

		then:
		response.statusCode == 200
		def parsedBody = parser.parseText(response.body)
		parsedBody.playerId == hostPlayerId
		parsedBody.opponentId == opponentId
		parsedBody.state == 'ACTIVE'
	}

	def "should return conflict when player tries to create a new game with max limit reached"() {
		given:
		def hostPlayerId = UUID.randomUUID().toString()
		generator.generateGames(hostPlayerId, 2)
		def body = """
		{
			"opponentId": "${UUID.randomUUID().toString()}"
		}
		""".stripIndent()
		def request = ApiGatewayEventFactory.create(body, hostPlayerId)

		when:
		def response = uat.handleRequest(request, null)

		then:
		response.statusCode == 409
		response.body == "Maximum number of active games reached, finish your games first"
	}
}
