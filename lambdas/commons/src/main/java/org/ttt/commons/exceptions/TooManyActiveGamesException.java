package org.ttt.commons.exceptions;

public class TooManyActiveGamesException extends Exception {
  public TooManyActiveGamesException(String message) {
    super(message);
  }
}
