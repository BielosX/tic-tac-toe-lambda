locals {
  games_table_write_capacity = 2
  games_table_read_capacity  = 2
}

resource "aws_dynamodb_table" "games_table" {
  hash_key       = "gameId"
  name           = "games"
  billing_mode   = "PROVISIONED"
  read_capacity  = local.games_table_read_capacity
  write_capacity = local.games_table_write_capacity

  attribute {
    name = "gameId"
    type = "S"
  }

  attribute {
    name = "playerId"
    type = "S"
  }

  attribute {
    name = "opponentId"
    type = "S"
  }

  global_secondary_index {
    hash_key        = "playerId"
    range_key       = "gameId"
    name            = "PlayerIdIndex"
    projection_type = "ALL"
    write_capacity  = local.games_table_write_capacity
    read_capacity   = local.games_table_read_capacity
  }

  global_secondary_index {
    hash_key        = "opponentId"
    range_key       = "gameId"
    name            = "OpponentIdIndex"
    projection_type = "ALL"
    write_capacity  = local.games_table_write_capacity
    read_capacity   = local.games_table_read_capacity
  }
}

resource "aws_dynamodb_table" "games_count_table" {
  hash_key       = "playerId"
  name           = "games-count"
  billing_mode   = "PROVISIONED"
  read_capacity  = 2
  write_capacity = 2

  attribute {
    name = "playerId"
    type = "S"
  }
}