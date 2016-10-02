# sleep 30 sec to wait docker ready
sleep 30

python dynamo-local-test.py
python titan-test.py
