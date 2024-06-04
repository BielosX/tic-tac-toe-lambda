resource "aws_dynamodb_table" "games_table" {
  hash_key       = "gameId"
  name           = "games"
  billing_mode   = "PROVISIONED"
  read_capacity  = 2
  write_capacity = 2

  attribute {
    name = "gameId"
    type = "S"
  }

  attribute {
    name = "playerId"
    type = "S"
  }

  global_secondary_index {
    hash_key        = "playerId"
    range_key       = "gameId"
    name            = "PlayerIdIndex"
    projection_type = "ALL"
    write_capacity  = 2
    read_capacity   = 2
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