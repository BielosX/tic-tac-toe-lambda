package org.ttt.commons;

public record CommitMoveRequest(
    Integer round, Integer positionX, Integer positionY, GameSymbol symbol) {}
