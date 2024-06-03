Feature: Game

  Scenario: Create a game
    When Player "471e16df-96c9-4aed-ad45-ed5f5cca939b" creates a game with opponent "f92bbcd2-21c9-4154-a8cb-0ce993f84747"
    Then Game is created for player "471e16df-96c9-4aed-ad45-ed5f5cca939b" and opponent "f92bbcd2-21c9-4154-a8cb-0ce993f84747"
