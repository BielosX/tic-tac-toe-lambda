package org.ttt.startgame

import org.testcontainers.containers.localstack.LocalStackContainer
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

class LocalStackDynamoDbClientFactory {
	static DynamoDbClient create(LocalStackContainer container) {
		return DynamoDbClient.builder()
				.region(Region.of(container.region))
				.endpointOverride(container.endpoint)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(container.accessKey, container.secretKey))).build()
	}
}
