#!/bin/bash

set -x

awslocal s3 mb s3://file-store

awslocal dynamodb create-table \
   --table-name Payments \
   --attribute-definitions AttributeName=Id,AttributeType=S \
   --key-schema AttributeName=Id,KeyType=HASH \
   --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
