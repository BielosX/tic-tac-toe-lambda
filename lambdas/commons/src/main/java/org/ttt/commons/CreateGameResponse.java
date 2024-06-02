package org.ttt.commons;

import java.time.ZonedDateTime;

public record CreateGameResponse(
    String gameId,
    String playerId,
    String opponent,
    ZonedDateTime lastUpdate,
    ZonedDateTime created) {}
