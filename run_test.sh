# sleep 30 sec to wait docker ready
sleep 30

python tests/dynamo-local-test.py
python tests/titan-test.py
