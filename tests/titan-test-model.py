# This is a python 3.4 test script using bulbsflow to connect to titan
#
# First install bulbsflow:
# - pip install bulbs
#

from bulbs.titan import Graph, Config, KeyIndex
from bulbs.config import DEBUG, ERROR
from bulbs.model import Node, Relationship
from bulbs.property import String, Integer, DateTime
from bulbs.utils import current_datetime

class Titan(Node):
    element_type = 'titan'

    titan_name = String(nullable=False, unique=True, indexed=True)
    titan_age = Integer()

class Father(Relationship):
    label = 'titan_father'

config = Config("http://localhost:8182/graphs/yourdatabasename/")
g = Graph(config)

g.config.set_logger(DEBUG)
g.scripts.update('gremlin.groovy')  # add file to scripts index
g.gremlin.command(g.scripts.get('createSchema'), dict())

g.add_proxy('titan', Titan)
g.add_proxy('father', Father)

# create account nodes
saturn = g.titan.get_or_create('titan_name', 'saturn', {'titan_name': 'saturn', 'titan_age': 10000})
jupiter = g.titan.get_or_create('titan_name', 'jupiter', {'titan_name': 'jupiter', 'titan_age': 5000})
hercules = g.titan.get_or_create('titan_name', 'hercules', {'titan_name': 'hercules', 'titan_age': 30})
nobody = g.titan.index.lookup(account_name='nobody')
# check return nodes
assert(str(saturn.get('titan_name'))=='saturn')
assert(jupiter.get('titan_age')==5000)
assert(nobody==None)

# creat relationship
try:
    g.father.create(jupiter, saturn)
    g.father.create(hercules, jupiter)
except:
    pass

# get saturn's grandchild
hercules = g.gremlin.query(g.scripts.get('getGrandChild'), dict(key='titan_name', value='saturn', rel='titan_father'))
assert(str(hercules.next().get('titan_name'))=='hercules')
