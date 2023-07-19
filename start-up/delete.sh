# Turning off the AWS pager so that the CLI doesn't open an editor for each command result
export AWS_PAGER=""

aws cloudformation delete-stack \
  --stack-name s3-lambda-lambda-dynamo

  aws cloudformation wait stack-delete-complete \
    --stack-name s3-lambda-lambda-dynamo