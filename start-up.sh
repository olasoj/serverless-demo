#!/bin/bash

# Build the application
echo $PWD
echo "Building application"
./gradlew build

#Upload to build
echo "Upload artifact to S3 bucket"
aws s3 cp ./app/build/distributions/app.zip  s3://lambda-test-bucket-12-07-2023

#start cloud formation
echo "Creating cloud formation stack"
 ./start-up/create.sh

 #Uploading address.csv file to S3
 echo "Upload address.csv to S3 bucket"
 aws s3 cp ./app/src/main/resources/aws/data/address_data.csv  s3://lambda-test-bucket-1689763516

