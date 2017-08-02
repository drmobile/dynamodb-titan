import os
from unittest import TestCase

import boto3
import gremlin_python.driver.client
import pytest
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection
from gremlin_python.structure.graph import Graph


class DynamoLocalTestCase(TestCase):
    def test_tables_existed(self):
        ddb = boto3.resource(
            'dynamodb',
            endpoint_url='http://localhost:8000',
            aws_access_key_id='',
            aws_secret_access_key='',
        )

        tables = list(ddb.tables.all())
        tables = [t.name for t in tables]

        assert 'yourdatabasename_edgestore' in tables
        assert 'yourdatabasename_graphindex' in tables
        assert 'yourdatabasename_system_properties' in tables
        assert 'yourdatabasename_systemlog' in tables
        assert 'yourdatabasename_janusgraph_ids' in tables
        assert 'yourdatabasename_txlog' in tables


class GremlinPythonTestCase(TestCase):
    def setUp(self):
        self.graph = Graph()
        self.g = self.graph.traversal().withRemote(DriverRemoteConnection('ws://localhost:8182/gremlin', 'g'))
        self.client = gremlin_python.driver.client.Client('ws://localhost:8182/gremlin', 'g')

        # schema define
        with open(os.path.dirname(__file__) + '/scripts/schema.groovy', 'r') as schema_f_obj:
            ret = self.client.submit(schema_f_obj.read())
            ret.next()

    def tearDown(self):
        self.g.V().drop().iterate()
        self.g.E().drop().iterate()

    def test_addV(self):
        self.g.addV('person').property('name', 'John').next()
        assert 'John' in self.g.V().name.toList()

    def test_addE(self):
        self.g.addV('person').property('name', 'John').next()
        self.g.addV('person').property('name', 'Ana').next()
        self.g.V().has('name', 'John').addE('follow').to(self.g.V().has('name', 'Ana')).next()
        assert self.g.V().has('name', 'John').out('follow').has('name', 'Ana').next()

    def test_index(self):
        with open(os.path.dirname(__file__) + '/scripts/indexes.groovy', 'r') as index_f_obj:
            ret = self.client.submit(index_f_obj.read())
            ret.next()
