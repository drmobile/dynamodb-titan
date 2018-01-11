from bulbs.titan import Graph, Config

config = Config("http://localhost:8182/graphs/yourdatabasename/")
g = Graph(config)

g.gremlin.command(
    """
    v1 = g.addVertex([account_id: 1])
    v2 = g.addVertex([account_id: 2])
    g.addEdge(v1, v2, 'follow')
    """
)
vertex = g.gremlin.query("g.V().has('account_id', 1).out('follow')")

assert vertex.next().get('account_id') == 2
