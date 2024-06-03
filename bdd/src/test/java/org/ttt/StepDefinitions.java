package org.ttt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions {

  private final GamesClient gamesClient;
  private String gameId;

  public StepDefinitions() {
    String apiUrl = System.getenv("API_URL");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    gamesClient =
        Feign.builder()
            .decoder(new JacksonDecoder(mapper))
            .encoder(new JacksonEncoder(mapper))
            .target(GamesClient.class, apiUrl);
  }

  @When("Player {string} creates a game with opponent {string}")
  public void player_creates_a_game_with_opponent(String player, String opponent) {
    this.gameId = gamesClient.createNewGame(new CreateGameRequest(opponent), player).gameId();
  }

  @Then("Game is created for player {string} and opponent {string}")
  public void game_is_created_for_player_and_opponent(String player, String opponent) {
    GetGameResponse response = gamesClient.getGameById(gameId);
    assertThat(response).isNotNull();
    assertThat(response.playerId()).isEqualTo(player);
    assertThat(response.opponent()).isEqualTo(opponent);
  }
}
