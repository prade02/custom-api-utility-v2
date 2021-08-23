#!/bin/bash
aws lambda update-function-code --function-name multi-utility-v2 \
--zip-file fileb://build/libs/sample-gradle-project-1.0.jar
