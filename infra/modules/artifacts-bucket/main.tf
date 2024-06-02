data "aws_caller_identity" "current" {}
data "aws_region" "current" {}

module "s3_bucket" {
  source = "terraform-aws-modules/s3-bucket/aws"

  bucket = "artifacts-${data.aws_region.current.name}-${data.aws_caller_identity.current.account_id}"

  control_object_ownership = true
  object_ownership         = "BucketOwnerEnforced"
  ignore_public_acls       = true
  block_public_acls        = true
  block_public_policy      = true
  restrict_public_buckets  = true
  force_destroy            = true
}

resource "aws_ssm_parameter" "deployment_id" {
  name  = "/tic-tac-toe/deployment-id"
  type  = "String"
  value = "0"
  lifecycle {
    ignore_changes = [value]
  }
}