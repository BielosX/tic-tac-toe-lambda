provider "aws" {
  region = "eu-west-1"
}

terraform {
  backend "s3" {
    key    = "artifacts-bucket.tfstate"
    region = "eu-west-1"
  }
  required_providers {
    aws = {
      version = ">= 5.51.1"
      source  = "hashicorp/aws"
    }
  }
  required_version = ">= 1.7.1"
}

module "artifacts_bucket" {
  source = "../../modules/artifacts-bucket"
}