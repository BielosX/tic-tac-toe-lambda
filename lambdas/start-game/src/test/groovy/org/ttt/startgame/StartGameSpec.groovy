package org.ttt.startgame

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import groovy.json.JsonSlurper
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import org.ttt.commons.ConstParametersProvider
import org.ttt.commons.GamesService
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import spock.lang.Shared
import spock.lang.Specification

class StartGameSpec extends Specification {
	@Shared
	def localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
	.withServices(LocalStackContainer.Service.DYNAMODB)
	@Shared
	def parser = new JsonSlurper()
	@Shared
	def uat

	def setupSpec() {
		localstack.start()
		def dynamoClient = DynamoDbClient.builder()
				.endpointOverride(localstack.endpoint)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(localstack.accessKey, localstack.secretKey)))
				.region(Region.of(localstack.region))
				.build()
		def gamesTableName = "games-table"
		def gamesCountTableName = "games-count-table"
		TableBuilder.createGamesTable(dynamoClient, gamesTableName)
		TableBuilder.createGamesCountTable(dynamoClient, gamesCountTableName)
		def parametersProvider = new ConstParametersProvider()
		parametersProvider.setParameter("GAMES_TABLE_NAME", gamesTableName)
		parametersProvider.setParameter("GAMES_COUNT_TABLE_NAME", gamesCountTableName)
		parametersProvider.setParameter("MAX_GAMES_COUNT", "2")
		def gameService = new GamesService(parametersProvider, dynamoClient)
		uat = new StartGame(gameService)
	}

	def cleanupSpec() {
		localstack.stop()
	}

	def "return newly created game"() {
		given:
		def hostPlayerId = UUID.randomUUID().toString()
		def opponentId = UUID.randomUUID().toString()
		def body = """
		{
			"opponentId": "$opponentId"
		}
		""".stripIndent()
		def request = APIGatewayV2HTTPEvent.builder()
				.withBody(body)
				.withHeaders([playerid: hostPlayerId])
				.build()

		when:
		def response = uat.handleRequest(request, null)

		then:
		response.statusCode == 200
		def parsedBody = parser.parseText(response.body)
		parsedBody.playerId == hostPlayerId
		parsedBody.opponentId == opponentId
		parsedBody.state == 'ACTIVE'
	}
}
