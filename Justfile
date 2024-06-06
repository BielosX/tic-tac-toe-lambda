export AWS_REGION := "eu-west-1"
export AWS_PAGER := ""
name-prefix := "tic-tac-toe"
stack-name := name-prefix + "-backend"
timestamp := `date +%s`
deployment-id-parameter := "/tic-tac-toe/deployment-id"
stage-url-parameter := "/tic-tac-toe/stage-url"
auth-issuer := ""
auth-audience := ""

format-tf:
    tofu fmt -recursive "{{ justfile_directory() }}"

format-java:
    {{ justfile_directory() }}/gradlew spotlessJavaApply

deploy-backend:
    aws cloudformation deploy \
      --template-file "{{ justfile_directory() }}/infra/backend.yaml" \
      --stack-name "{{ stack-name }}" \
      --parameter-overrides "NamePrefix={{ name-prefix }}"

tofu-init-artifacts-bucket:
    #!/bin/bash
    state_bucket=$(aws ssm get-parameter --name "{{name-prefix}}-state-bucket" | jq -r '.Parameter.Value')
    lock_table=$(aws ssm get-parameter --name "{{name-prefix}}-lock-table" | jq -r '.Parameter.Value')
    tofu -chdir="{{ justfile_directory() }}/infra/live/artifacts-bucket" init \
      -backend-config="bucket=${state_bucket}" \
      -backend-config="dynamodb_table=${lock_table}"

deploy-artifacts-bucket: deploy-backend tofu-init-artifacts-bucket
    tofu -chdir="{{ justfile_directory() }}/infra/live/artifacts-bucket" apply -auto-approve

tofu-init-lambda:
    #!/bin/bash
    state_bucket=$(aws ssm get-parameter --name "{{name-prefix}}-state-bucket" | jq -r '.Parameter.Value')
    lock_table=$(aws ssm get-parameter --name "{{name-prefix}}-lock-table" | jq -r '.Parameter.Value')
    tofu -chdir="{{ justfile_directory() }}/infra/live/lambda" init \
      -backend-config="bucket=${state_bucket}" \
      -backend-config="dynamodb_table=${lock_table}"

deploy-packages: deploy-artifacts-bucket
    #!/bin/bash -e
    {{ justfile_directory() }}/gradlew clean build :commons:buildZip copyJar
    artifacts_bucket=$(tofu -chdir="{{ justfile_directory() }}/infra/live/artifacts-bucket" output -raw bucket_id)
    export PIPENV_PIPFILE="{{ justfile_directory() }}/scripts/Pipfile"
    pipenv install
    pipenv run python "{{ justfile_directory() }}/scripts/upload-artifacts.py" "{{ justfile_directory() }}/build" \
      "${artifacts_bucket}" "{{ timestamp }}"
    aws ssm put-parameter --name "{{ deployment-id-parameter }}" --value "{{ timestamp }}" --overwrite

deploy-lambda: deploy-artifacts-bucket tofu-init-lambda deploy-packages
    #!/bin/bash -e
    state_bucket=$(aws ssm get-parameter --name "{{name-prefix}}-state-bucket" | jq -r '.Parameter.Value')
    deployment_id=$(aws ssm get-parameter --name "{{ deployment-id-parameter }}" | jq -r '.Parameter.Value')
    tofu -chdir="{{ justfile_directory() }}/infra/live/lambda" apply -auto-approve \
        -var="deployment_id=${deployment_id}" -var="artifacts_bucket_backend_bucket=${state_bucket}" \
        -var="audience={{ auth-audience }}" -var="issuer={{ auth-issuer }}"

deploy: deploy-backend deploy-artifacts-bucket deploy-lambda

destroy-artifacts-bucket:
    tofu -chdir="{{ justfile_directory() }}/infra/live/artifacts-bucket" destroy -auto-approve

destroy-lambda:
    #!/bin/bash -e
    state_bucket=$(aws ssm get-parameter --name "{{name-prefix}}-state-bucket" | jq -r '.Parameter.Value')
    tofu -chdir="{{ justfile_directory() }}/infra/live/lambda" destroy -auto-approve \
        -var="deployment_id=''" -var="artifacts_bucket_backend_bucket=${state_bucket}" \
        -var="audience=''" -var="issuer=https://dummy.com/"

clean-bucket:
    #!/bin/bash
    state_bucket=$(aws ssm get-parameter --name "{{name-prefix}}-state-bucket" | jq -r '.Parameter.Value')
    export PIPENV_PIPFILE="{{ justfile_directory() }}/scripts/Pipfile"
    pipenv install
    pipenv run python "{{ justfile_directory() }}/scripts/clean-bucket.py" "${state_bucket}"

delete-backend: clean-bucket
    #!/bin/bash -e
    export PIPENV_PIPFILE="{{ justfile_directory() }}/scripts/Pipfile"
    pipenv install
    pipenv run python "{{ justfile_directory() }}/scripts/delete-stack.py" "{{ stack-name }}"

destroy: destroy-lambda destroy-artifacts-bucket delete-backend

doc:
    redocly build-docs -o index.html "{{ justfile_directory() }}/spec/openapi.yaml"

bdd:
    #!/bin/bash -e
    stage_url=$(aws ssm get-parameter --name "{{ stage-url-parameter }}" | jq -r '.Parameter.Value')
    export API_URL="${stage_url}"
    {{ justfile_directory() }}/gradlew clean :bdd:test -PbddTests
