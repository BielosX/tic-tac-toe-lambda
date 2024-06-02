provider "aws" {
  region = "eu-west-1"
}

terraform {
  backend "s3" {
    key = "lambda.tfstate"
  }
  required_providers {
    aws = {
      version = ">= 5.51.1"
      source  = "hashicorp/aws"
    }
  }
  required_version = ">= 1.7.1"
}

data "terraform_remote_state" "artifacts_bucket" {
  backend = "s3"
  config = {
    key    = "artifacts-bucket.tfstate"
    bucket = var.artifacts_bucket_backend_bucket
    region = "eu-west-1"
  }
}

module "lambda" {
  source           = "../../modules/lambda"
  artifacts_bucket = data.terraform_remote_state.artifacts_bucket.outputs.bucket_id
  deployment_id    = var.deployment_id
}