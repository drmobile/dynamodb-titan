#!/bin/bash

function run_awslogs {
    ORI=/var/awslogs/etc/awslogs.conf
    NEW=/var/awslogs/etc/awslogs.conf.new
    OLD=/var/awslogs/etc/awslogs.conf.old

    # make a copy from original
    cp $ORI $NEW
    cp $ORI $OLD

    # replace stream name as version
    version=$CONTAINER_VERSION
    sed -i "s/_VERSION_/${version}/g" $NEW

    # replace group name as graph name with 'titan.' prefix
    sed -i "s/_LOG_GROUP_NAME_/titan.${GRAPH_NAME}/g" $NEW

    # replace original with new one
    cp $NEW $ORI

    # restart awslogs agent
    service awslogs restart
}

function run_titan {
    BIN=./bin
    SLEEP_INTERVAL_S=2

    IN=rexster-dynamodb.xml.template
    OUT=rexster-dynamodb.xml

    cp $IN $OUT

    # Use hash instead of slash because the hostport has a slash
    sed -i "s#_DYNAMODB_HOSTPORT_#${DYNAMODB_HOSTPORT}#g" $OUT
    sed -i "s#_GRAPH_NAME_#${GRAPH_NAME}#g" $OUT
    sed -i "s#_BASE_URI_#${BASE_URI}#g" $OUT

    $BIN/rexster.sh -s -c ../$OUT
}

run_awslogs
run_titan