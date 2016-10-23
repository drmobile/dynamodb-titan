# This is a python 3.4 test script using bulbsflow to connect to titan
#
# First install bulbsflow:
# - pip install bulbs
#

from bulbs.titan import Graph, Config
from bulbs.config import DEBUG, ERROR

config = Config("http://localhost:8182/graphs/yourdatabasename/")
g = Graph(config)

#g.config.set_logger(DEBUG)
g.scripts.update('gremlin.groovy')  # add file to scripts index

# create Graph Of The Gods by loading GraphOfTheGodsFactory from Titan backend
#g.gremlin.command(g.scripts.get('loadGraphOfTheGodsFactory'), dict())

# create Graph Of The Gods from gremlin script
g.gremlin.command(g.scripts.get('loadGraphOfTheGodsFactory2'), dict())

'''
Testing scenarios
'''

# locate saturn node
saturn = g.vertices.get_or_create('name', 'saturn')
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
