#!/bin/bash

set -x

awslocal s3 mb s3://file-store

awslocal dynamodb create-table \
   --table-name Payments \
   --attribute-definitions AttributeName=Reference,AttributeType=S \
   --key-schema AttributeName=Reference,KeyType=HASH \
   --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
