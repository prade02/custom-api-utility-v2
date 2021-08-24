#!/bin/bash
aws lambda update-function-code --function-name multi-utility-v2 \
--zip-file fileb://build/libs/custom-api-utility-v2-1.0.jar
