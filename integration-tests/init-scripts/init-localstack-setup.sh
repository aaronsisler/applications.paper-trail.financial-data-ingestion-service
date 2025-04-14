#!/bin/sh
echo "Begin: SQS -> Create Topic"
#
awslocal sqs create-queue \
    --region us-east-1 \
    --cli-input-json file://sqs-topic-definition.json
#
echo "End: SQS -> Create Topic"