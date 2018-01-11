#!/usr/bin/env bash

# sleep 30 sec to wait docker ready
sleep 30

python dynamo-local-test.py
python titan-test-gremlin.py
python titan-test-model.py

docker-compose exec titan-inmemory python /tmp/test.py