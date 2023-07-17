# Turning off the AWS pager so that the CLI doesn't open an editor for each command result
export AWS_PAGER=""

aws cloudformation create-stack \
  --stack-name s3-lambda-lambda-dynamo \
  --template-body file://template.yml \
  --capabilities CAPABILITY_IAM