// Composite Index
graph.tx().rollback() //Never create new indexes while a transaction is active
mgmt = graph.openManagement()
name = mgmt.getPropertyKey('name')
age = mgmt.getPropertyKey('age')
mgmt.buildIndex('byNameComposite', Vertex.class).addKey(name).buildCompositeIndex()
mgmt.buildIndex('byNameAndAgeComposite', Vertex.class).addKey(name).addKey(age).buildCompositeIndex()
mgmt.commit()
//Wait for the index to become available
mgmt.awaitGraphIndexStatus(graph, 'byNameComposite').call()
mgmt.awaitGraphIndexStatus(graph, 'byNameAndAgeComposite').call()

// Vertex-centric Indexes
graph.tx().rollback()  //Never create new indexes while a transaction is active
mgmt = graph.openManagement()
time = mgmt.getPropertyKey('time')
battled = mgmt.getEdgeLabel('battled')
mgmt.buildEdgeIndex(battled, 'battlesByTime', Direction.BOTH, Order.decr, time)
mgmt.commit()
mgmt.awaitRelationIndexStatus(graph, 'battlesByTime', 'battled').call()

return null