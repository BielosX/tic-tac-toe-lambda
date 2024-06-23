package org.ttt.commons.exceptions;

public class GameAlreadyFinishedException extends Exception {
  public GameAlreadyFinishedException(String message) {
    super(message);
  }
}
