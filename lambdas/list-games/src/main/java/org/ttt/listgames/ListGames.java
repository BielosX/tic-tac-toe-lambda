package org.ttt.listgames;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ttt.commons.GamesListPage;
import org.ttt.commons.GamesService;
import org.ttt.commons.ObjectMapperFactory;
import org.ttt.commons.SubjectAwareRequestHandler;

@Slf4j
@SuppressWarnings("unused")
public class ListGames extends SubjectAwareRequestHandler {
  private static final String AS_OPPONENT_PARAM = "asOpponent";
  private static final String LIMIT_PARAM = "limit";
  private static final String NEXT_PAGE_TOKEN = "nextPageToken";
  private static final int DEFAULT_PAGE_SIZE = 10;
  private final ObjectMapper objectMapper = ObjectMapperFactory.create();
  private final GamesService gamesService;

  public ListGames(GamesService gamesService) {
    this.gamesService = gamesService;
  }

  public ListGames() {
    this.gamesService = new GamesService();
  }

  @Override
  protected APIGatewayV2HTTPResponse handleRequestWithSubject(
      String subject, APIGatewayV2HTTPEvent event, Context context) {
    Optional<Map<String, String>> queryParams =
        Optional.ofNullable(event.getQueryStringParameters());
    boolean asOpponent =
        queryParams
            .flatMap(p -> Optional.ofNullable(p.get(AS_OPPONENT_PARAM)))
            .map(Boolean::parseBoolean)
            .orElse(false);
    int pageSize =
        queryParams
            .flatMap(p -> Optional.ofNullable(p.get(LIMIT_PARAM)))
            .map(Integer::parseInt)
            .orElse(DEFAULT_PAGE_SIZE);
    String nextPageToken =
        queryParams.flatMap(p -> Optional.ofNullable(p.get(NEXT_PAGE_TOKEN))).orElse(null);
    GamesListPage page = gamesService.listGames(subject, asOpponent, pageSize, nextPageToken);
    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withBody(pageToJson(page))
        .build();
  }

  @SneakyThrows
  private String pageToJson(GamesListPage page) {
    return objectMapper.writeValueAsString(page);
  }
}
