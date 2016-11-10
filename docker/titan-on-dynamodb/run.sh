#!/bin/bash

BIN=./bin

IN=dynamodb.properties.template
OUT=dynamodb.properties

cp conf/gremlin-server/$IN conf/gremlin-server/$OUT
ls conf/gremlin-server/

# Use hash instead of slash because the hostport has a slash
sed -i "s#_DYNAMODB_HOSTPORT_#${DYNAMODB_HOSTPORT}#g" conf/gremlin-server/$OUT
sed -i "s#_GRAPH_NAME_#${GRAPH_NAME}#g" conf/gremlin-server/$OUT

$BIN/gremlin-server.sh ${PWD}/conf/gremlin-server/gremlin-server.yaml

