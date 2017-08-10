
static def createEdgeLabel(mgmt, String label, multi) {
    if (!mgmt.containsEdgeLabel(label)) {
        return mgmt.makeEdgeLabel(label).multiplicity(multi).make()
    }
}

static def createPropertyKey(mgmt, String key, type, card) {
    if (!mgmt.containsPropertyKey(key)) {
        mgmt.makePropertyKey(key).dataType(type).cardinality(card).make()
    }
}

static def createVertexLabel(mgmt, String label) {
    if (!mgmt.containsVertexLabel(label)) {
        mgmt.makeVertexLabel(label).make()
    }
}

static def buildSchema(graph) {
    def mgmt = graph.openManagement()

    // edges
    createEdgeLabel(mgmt, 'follow', MULTI)
    createEdgeLabel(mgmt, 'mother', MANY2ONE)
    createEdgeLabel(mgmt, 'battled', MULTI)
    // properties
    createPropertyKey(mgmt, 'birthDate', Long.class, Cardinality.SINGLE)
    createPropertyKey(mgmt, 'name', String.class, Cardinality.SET)
    createPropertyKey(mgmt, 'age', Integer.class, Cardinality.SINGLE)
    createPropertyKey(mgmt, 'time', Date.class, Cardinality.SINGLE)
    createPropertyKey(mgmt, 'sensorReading', Double.class, Cardinality.LIST)
    // vertices
    createVertexLabel(mgmt, 'person')

    mgmt.commit()
}

static def buildIndex(graph) {
    // Composite Index
    def mgmt = graph.openManagement()
    if (mgmt.getGraphIndex('byNameComposite') != null) {
        return null  // already built index
    }

    graph.tx().rollback() //Never create new init while a transaction is active
    def name = mgmt.getPropertyKey('name')
    def age = mgmt.getPropertyKey('age')
    mgmt.buildIndex('byNameComposite', Vertex.class).addKey(name).buildCompositeIndex()
    mgmt.buildIndex('byNameAndAgeComposite', Vertex.class).addKey(name).addKey(age).buildCompositeIndex()
    mgmt.commit()
    //Wait for the init to become available
    mgmt.awaitGraphIndexStatus(graph, 'byNameComposite').call()
    mgmt.awaitGraphIndexStatus(graph, 'byNameAndAgeComposite').call()

    // Vertex-centric Indexes
    graph.tx().rollback()  //Never create new init while a transaction is active
    mgmt = graph.openManagement()
    def time = mgmt.getPropertyKey('time')
    def battled = mgmt.getEdgeLabel('battled')
    mgmt.buildEdgeIndex(battled, 'battlesByTime', Direction.BOTH, Order.decr, time)
    mgmt.commit()
    mgmt.awaitRelationIndexStatus(graph, 'battlesByTime', 'battled').call()
}

// an init script that returns a Map allows explicit setting of global bindings.
def globals = [:]

// defines a sample LifeCycleHook that prints some output to the Gremlin Server console.
// note that the name of the key in the "global" map is unimportant.
globals << [hook: [
        onStartUp : { ctx ->
            ctx.logger.info("Building schema...")
            buildSchema(graph)
            ctx.logger.info("Finished building schema.")
            ctx.logger.info("Building init...")
            buildIndex(graph)
            ctx.logger.info("Finished building init.")
        },
        onShutDown: { ctx ->
            ctx.logger.info("Executed once at shutdown of Gremlin Server.")
        }
] as LifeCycleHook]

// define the default TraversalSource to bind queries to - this one will be named "g".
globals << [g: graph.traversal()]