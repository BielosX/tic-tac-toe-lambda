package org.ttt.describegame

import org.ttt.commons.ApiGatewayEventFactory
import org.ttt.commons.Game
import org.ttt.commons.GamesService
import spock.lang.Specification

class DescribeGameSpec extends Specification {
	GamesService gameService = Stub()
	def uat = new DescribeGame(gameService)
	def playerId = UUID.randomUUID().toString()

	def "should return 404 when game not found"() {
		given:
		def gameId = UUID.randomUUID().toString()
		def request = ApiGatewayEventFactory.create("", playerId)
		request.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(request, null)

		then:
		response.statusCode == 404
	}

	def "should return 200 when game found"() {
		given:
		def gameId = UUID.randomUUID().toString()
		gameService.getGame(gameId) >> Optional.of(Game.builder()
				.gameId(gameId)
				.build())
		def request = ApiGatewayEventFactory.create("", playerId)
		request.pathParameters = [gameId: gameId]

		when:
		def response = uat.handleRequest(request, null)

		then:
		response.statusCode == 200
	}
}
