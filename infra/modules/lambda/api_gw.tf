resource "aws_apigatewayv2_api" "api" {
  name             = "api"
  protocol_type    = "HTTP"
  fail_on_warnings = true
  body = templatefile("${path.module}/../../../spec/openapi.yaml", {
    player_move_arn : aws_lambda_function.lambda["player-move"].invoke_arn,
    start_game_arn : aws_lambda_function.lambda["start-game"].invoke_arn,
    describe_game_arn : aws_lambda_function.lambda["describe-game"].invoke_arn
    issuer : var.issuer
    audience : var.audience
  })
}

resource "aws_apigatewayv2_stage" "stage" {
  api_id      = aws_apigatewayv2_api.api.id
  name        = "$default"
  auto_deploy = true
}

resource "aws_ssm_parameter" "stage_url" {
  name  = "/tic-tac-toe/stage-url"
  type  = "String"
  value = aws_apigatewayv2_stage.stage.invoke_url
}