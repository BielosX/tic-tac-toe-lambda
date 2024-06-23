package org.ttt.commons

import org.ttt.commons.exceptions.GameAlreadyFinishedException
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
}
