# This is a python 3.4 test script using boto to connect to the localhost dynamodb-local instance
#
# First install boto:
# - pip install boto


from boto.dynamodb2.layer1 import DynamoDBConnection


connection = DynamoDBConnection(
    aws_access_key_id='foo',         # Dummy access key
    aws_secret_access_key='bar',     # Dummy secret key
    host='localhost',                # Host where DynamoDB Local resides
    port=8000,                       # DynamoDB Local port (8000 is the default)
    is_secure=False)                 # Disable secure connections

tables = connection.list_tables()

print(connection.list_tables())
assert(tables['TableNames'][0] == 'yourdatabasename_edgestore')
assert(tables['TableNames'][1] == 'yourdatabasename_graphindex')
assert(tables['TableNames'][2] == 'yourdatabasename_system_properties')
assert(tables['TableNames'][3] == 'yourdatabasename_systemlog')
assert(tables['TableNames'][4] == 'yourdatabasename_titan_ids')
assert(tables['TableNames'][5] == 'yourdatabasename_txlog')



