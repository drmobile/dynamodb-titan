#!/bin/bash

# Use hash instead of slash because the hostport has a slash
sed -i "s#_GRAPH_NAME_#${GRAPH_NAME}#g" rexster-dynamodb.xml
sed -i "s#_BASE_URI_#${BASE_URI}#g" rexster-dynamodb.xml

./bin/rexster.sh -s -c ../rexster-dynamodb.xml
