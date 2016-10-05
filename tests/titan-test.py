# This is a python 3.4 test script using bulbsflow to connect to titan
#
# First install bulbsflow:
# - pip install bulbs
#

from bulbs.titan import Graph, Config
from bulbs.config import DEBUG, ERROR

config = Config("http://localhost:8182/graphs/yourdatabasename/")
g = Graph(config)

g.config.set_logger(DEBUG)
g.scripts.update('gremlin.groovy')  # add file to scripts index

g.gremlin.command(g.scripts.get('loadGraphOfTheGodsFactory'), dict())

# locate saturn node
saturn = g.vertices.get_or_create('name', 'saturn')
# index name
assert(saturn.get_index_name(config)=='vertex')
# get saturn's grandchild
hercules = g.gremlin.query(g.scripts.get('getGrandChild'), dict(key='name',value='saturn', rel='father'))
assert(str(hercules.next().get('name'))=='hercules')
