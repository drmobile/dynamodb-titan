mgmt = graph.openManagement()

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
// Vertices
createVertexLabel(mgmt, 'person')

mgmt.commit()