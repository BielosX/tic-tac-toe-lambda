package org.ttt;

import java.time.ZonedDateTime;

public record GetGameResponse(
    String gameId,
    String playerId,
    String opponent,
    ZonedDateTime lastUpdate,
    ZonedDateTime created) {}
