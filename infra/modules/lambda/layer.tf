resource "aws_lambda_layer_version" "java_commons" {
  layer_name          = "java-commons"
  compatible_runtimes = ["java17"]
  s3_bucket           = var.artifacts_bucket
  s3_key              = "commons-${var.deployment_id}.zip"
}