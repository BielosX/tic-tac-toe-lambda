package org.ttt.commons;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

public class DynamoDbLocalStackContainer extends LocalStackContainer {
  private static final DockerImageName IMAGE_NAME =
      DockerImageName.parse("localstack/localstack:latest");

  public DynamoDbLocalStackContainer() {
    super(IMAGE_NAME);
    this.withServices(Service.DYNAMODB);
  }
}
