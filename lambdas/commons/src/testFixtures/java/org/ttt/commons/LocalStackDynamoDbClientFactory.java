package org.ttt.commons;

import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class LocalStackDynamoDbClientFactory {
  public static DynamoDbClient create(LocalStackContainer container) {
    AwsCredentials credentials =
        AwsBasicCredentials.create(container.getAccessKey(), container.getSecretKey());
    return DynamoDbClient.builder()
        .region(Region.of(container.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .endpointOverride(container.getEndpoint())
        .build();
  }
}
