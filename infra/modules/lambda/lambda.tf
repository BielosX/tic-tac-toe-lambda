locals {
  names = {
    "player-move" : "org.ttt.playermove.PlayerMove::handleRequest",
    "start-game" : "org.ttt.startgame.StartGame::handleRequest",
    "describe-game" : "org.ttt.describegame.DescribeGame::handleRequest"
  }
}

resource "aws_lambda_function" "lambda" {
  for_each      = local.names
  function_name = each.key
  role          = aws_iam_role.lambda_role.arn
  layers        = [aws_lambda_layer_version.java_commons.arn]
  timeout       = 60
  handler       = each.value
  runtime       = "java17"
  s3_key        = "${each.key}-${var.deployment_id}.jar"
  s3_bucket     = var.artifacts_bucket
  environment {
    variables = {
      GAMES_TABLE_NAME : aws_dynamodb_table.games_table.name
    }
  }
}

resource "aws_lambda_permission" "lambda_permission" {
  for_each      = toset(keys(local.names))
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda[each.key].function_name
  principal     = "apigateway.amazonaws.com"
  lifecycle {
    replace_triggered_by = [
      aws_lambda_function.lambda[each.key]
    ]
  }
}