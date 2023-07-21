# AWS Lambda demo
The project comprises a Lambda function that reads address files dropped in an S3 bucket and then calls another Lamda function to add the address(es) to Dynamo DB.

All resources, including Dynamo DB and the S3 bucket, are provisioned via YAML files.

The address file is comma-delimited and contains street name, city, state, and country information; a file can contain one or more addresses.

# The execution/data flow is:
Address-file - &gt; s3 - &gt; lambda-1 - &gt; lambda-2 -&gt; dynamodb

## Run Locally

Start-up the project

```bash
 ./start-up.sh
```

Clean-up the project

```bash
    ./clean-up.sh
```
