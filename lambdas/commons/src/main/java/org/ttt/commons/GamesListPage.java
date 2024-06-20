package org.ttt.commons;

import java.util.List;

public record GamesListPage(List<Game> games, String nextPageToken) {}
