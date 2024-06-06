variable "deployment_id" {
  type = string
}

variable "artifacts_bucket" {
  type = string
}

// Trailing "/" might be required
variable "issuer" {
  type = string
  validation {
    condition = can(regex("^https://.+$", var.issuer))
    error_message = "Issuer should start with https://"
  }
}

variable "audience" {
  type = string
}