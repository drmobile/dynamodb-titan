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

    name = String()
    age = Integer()

class Father(Relationship):
    label = 'father'

config = Config("http://localhost:8182/graphs/yourdatabasename/")
g = Graph(config)

#g.config.set_logger(DEBUG)
g.scripts.update('gremlin.groovy')  # add file to scripts index

g.add_proxy('titan', Titan)
g.add_proxy('father', Father)

'''
Testing scenarios
'''

# locate saturn node
saturn = g.titan.get_or_create('name', 'saturn')
# index name
assert(saturn.get_index_name(config)=='vertex')
# get saturn's grandchild
hercules = g.gremlin.query(g.scripts.get('getGrandChild'), dict(name='saturn'))
assert(hercules.next().get('name')=='hercules')

'''
Index
'''
# simple query hercules battled with
battledWithHercules = g.gremlin.query(g.scripts.get('queryBattleWith'), dict(name='hercules'))
assert(battledWithHercules.next().get('name')=='cerberus')
assert(battledWithHercules.next().get('name')=='hydra')
assert(battledWithHercules.next().get('name')=='nemean')
# query with limit
battledWithHercules = g.gremlin.query(g.scripts.get('queryBattleWithLimit'), dict(name='hercules', limit=2))
assert(battledWithHercules.next().get('name')=='cerberus')
assert(battledWithHercules.next().get('name')=='hydra')
exceptCaught = False
try:
    battledWithHercules.next().get('name')
except:
    exceptCaught = True
    pass
assert(exceptCaught==True)
# query with condition and limit
battledWithHercules = g.gremlin.query(g.scripts.get('queryBattleWithConditionLimit'), dict(name='hercules', time=10, limit=1))
assert(battledWithHercules.next().get('name')=='hydra')
exceptCaught = False
try:
    battledWithHercules.next().get('name')
except:
    exceptCaught = True
    pass
assert(exceptCaught==True)

