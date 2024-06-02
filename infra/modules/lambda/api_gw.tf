resource "aws_apigatewayv2_api" "api" {
  name             = "api"
  protocol_type    = "HTTP"
  fail_on_warnings = true
  body = templatefile("${path.module}/openapi.yaml", {
    player_move_arn : aws_lambda_function.lambda["player-move"].invoke_arn
  })
}

resource "aws_apigatewayv2_stage" "stge" {
  api_id      = aws_apigatewayv2_api.api.id
  name        = "$default"
  auto_deploy = true
}