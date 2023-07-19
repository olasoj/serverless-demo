#!/bin/bash

#Removing address.csv from S3 bucket
 echo "Removing address.csv from S3 bucket"
 aws s3 rm  s3://lambda-test-bucket-1689763516/address_data.csv

#Deleting cloud formation
echo "Deleting cloud formation stack"
./start-up/delete.sh