package org.ttt.listgames

import groovy.json.JsonSlurper
import org.ttt.commons.ApiGatewayEventFactory
import org.ttt.commons.ConstParametersProvider
import org.ttt.commons.DynamoDbLocalStackContainer
import org.ttt.commons.GameGenerator
import org.ttt.commons.GamesService
import org.ttt.commons.LocalStackDynamoDbClientFactory
import org.ttt.commons.TableFactory
import spock.lang.Shared
import spock.lang.Specification

class ListGamesSpec extends Specification {
	@Shared
	def localstack = new DynamoDbLocalStackContainer()
	@Shared
	def parser = new JsonSlurper()
	@Shared
	GameGenerator generator
	@Shared
	ListGames uat

	def setupSpec() {
		localstack.start()
		def dynamoClient = LocalStackDynamoDbClientFactory.create(localstack)
		TableFactory.createAllTables(dynamoClient)
		def parametersProvider = new ConstParametersProvider([
			"GAMES_TABLE_NAME": TableFactory.defaultGamesTableName,
			"GAMES_COUNT_TABLE_NAME": TableFactory.defaultGamesCountTableName,
			"MAX_GAMES_COUNT": "10"
		])
		def gamesService = new GamesService(parametersProvider, dynamoClient)
		generator = new GameGenerator(gamesService)
		uat = new ListGames(gamesService)
	}

	def cleanupSpec() {
		localstack.stop()
	}

	def "should return new page when available"() {
		given:
		def playerId = UUID.randomUUID().toString()
		generator.generateGames(playerId, 9)

		when:
		def queryParams = [
			"limit": "5"
		]
		def firstResponse = uat.handleRequest(ApiGatewayEventFactory.create("", queryParams, playerId), null)
		def firstResponseBody = parser.parseText(firstResponse.body)
		queryParams = [
			"nextPageToken": firstResponseBody.nextPageToken as String,
			"limit": "5"
		]
		def secondEvent = ApiGatewayEventFactory.create("", queryParams, playerId)
		def secondResponse = uat.handleRequest(secondEvent, null)
		def secondResponseBody = parser.parseText(secondResponse.body)

		then:
		firstResponseBody.nextPageToken != null
		secondResponseBody.nextPageToken == null
		firstResponseBody.games.size() == 5
		secondResponseBody.games.size() == 4
	}

	def "should return empty list when no games available"() {
		given:
		def playerId = UUID.randomUUID().toString()

		when:
		def response = uat.handleRequest(ApiGatewayEventFactory.create("", playerId), null)

		then:
		def parsedResponse = parser.parseText(response.body)
		parsedResponse.games.size() == 0
	}

	def "should not include nextPageToken when there is no next page"() {
		given:
		def playerId = UUID.randomUUID().toString()
		generator.generateGames(playerId, 2)
		def queryParams = [
				"limit": "5"
		]

		when:
		def response = uat.handleRequest(ApiGatewayEventFactory.create("", queryParams, playerId), null)
		def parsedResponse = parser.parseText(response.body)

		then:
		parsedResponse.nextPageToken == null
	}
}
