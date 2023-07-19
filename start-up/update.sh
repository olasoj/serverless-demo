# Turning off the AWS pager so that the CLI doesn't open an editor for each command result
export AWS_PAGER=""

aws cloudformation update-stack \
  --stack-name s3-lambda-lambda-dynamo \
  --template-body file://app/src/main/resources/aws/template/template.yml \
  --capabilities CAPABILITY_AUTO_EXPAND

aws cloudformation wait stack-update-complete \
  --stack-name s3-lambda-lambda-dynamo

#  --capabilities CAPABILITY_IAM
# The capability CAPABILITY_IAM to allow the stack to make changes to IAM (Identity and Access Management) roles.
# An error occurred (InsufficientCapabilitiesException) when calling the CreateStack operation: Requires capabilities : [CAPABILITY_AUTO_EXPAND]