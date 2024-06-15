package org.ttt.commons.exceptions;

public class TooManyActiveGamesException extends RuntimeException {
  public TooManyActiveGamesException(String message) {
    super(message);
  }
}
