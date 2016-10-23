def loadGraphOfTheGodsFactory() {
    //GraphOfTheGodsFactory.load(g)
    mgmt = g.getManagementSystem();
    if (mgmt.getEdgeLabel('brother') == null) {
        com.thinkaurelius.titan.example.GraphOfTheGodsFactory.load(g)
    }
    return 0
}

def loadGraphOfTheGodsFactory2() {
    //Create Schema,
    // https://github.com/thinkaurelius/titan/blob/titan05/titan-core/src/main/java/com/thinkaurelius/titan/example/GraphOfTheGodsFactory.java
    mgmt = g.getManagementSystem();
    if (mgmt.getEdgeLabel('brother') == null) {
        name = mgmt.makePropertyKey("name").dataType(String.class).make();
        namei = mgmt.buildIndex("name",Vertex.class).addKey(name).unique().buildCompositeIndex();
        mgmt.setConsistency(namei, com.thinkaurelius.titan.core.schema.ConsistencyModifier.LOCK);
        age = mgmt.makePropertyKey("age").dataType(Integer.class).make();
        mgmt.buildIndex("vertices",Vertex.class).addKey(age).buildMixedIndex("search");

        time = mgmt.makePropertyKey("time").dataType(Integer.class).make();
        reason = mgmt.makePropertyKey("reason").dataType(String.class).make();
        place = mgmt.makePropertyKey("place").dataType(com.thinkaurelius.titan.core.attribute.Geoshape.class).make();
        eindex = mgmt.buildIndex("edges",Edge.class)
                .addKey(reason).addKey(place).buildMixedIndex("search");

        mgmt.makeEdgeLabel("father").multiplicity(com.thinkaurelius.titan.core.Multiplicity.MANY2ONE).make();
        mgmt.makeEdgeLabel("mother").multiplicity(com.thinkaurelius.titan.core.Multiplicity.MANY2ONE).make();
        battled = mgmt.makeEdgeLabel("battled").signature(time).make();
        mgmt.buildEdgeIndex(battled, "battlesByTime", Direction.BOTH, com.thinkaurelius.titan.core.Order.DESC, time);
        mgmt.makeEdgeLabel("lives").signature(reason).make();
        mgmt.makeEdgeLabel("pet").make();
        mgmt.makeEdgeLabel("brother").make();

        mgmt.makeVertexLabel("titan").make();
        mgmt.makeVertexLabel("location").make();
        mgmt.makeVertexLabel("god").make();
        mgmt.makeVertexLabel("demigod").make();
        mgmt.makeVertexLabel("human").make();
        mgmt.makeVertexLabel("monster").make();

        mgmt.commit();

        // Graph Of The Gods
        // http://s3.thinkaurelius.com/docs/titan/0.5.4/getting-started.html

        tx = g.newTransaction();
        // vertices

        saturn = tx.addVertexWithLabel("titan");
        saturn.setProperty("name", "saturn");
        saturn.setProperty("age", 10000);

        sky = tx.addVertexWithLabel("location");
        ElementHelper.setProperties(sky, "name", "sky");

        sea = tx.addVertexWithLabel("location");
        ElementHelper.setProperties(sea, "name", "sea");

        jupiter = tx.addVertexWithLabel("god");
        ElementHelper.setProperties(jupiter, "name", "jupiter", "age", 5000);

        neptune = tx.addVertexWithLabel("god");
        ElementHelper.setProperties(neptune, "name", "neptune", "age", 4500);

        hercules = tx.addVertexWithLabel("demigod");
        ElementHelper.setProperties(hercules, "name", "hercules", "age", 30);

        alcmene = tx.addVertexWithLabel("human");
        ElementHelper.setProperties(alcmene, "name", "alcmene", "age", 45);

        pluto = tx.addVertexWithLabel("god");
        ElementHelper.setProperties(pluto, "name", "pluto", "age", 4000);

        nemean = tx.addVertexWithLabel("monster");
        ElementHelper.setProperties(nemean, "name", "nemean");

        hydra = tx.addVertexWithLabel("monster");
        ElementHelper.setProperties(hydra, "name", "hydra");

        cerberus = tx.addVertexWithLabel("monster");
        ElementHelper.setProperties(cerberus, "name", "cerberus");

        tartarus = tx.addVertexWithLabel("location");
        ElementHelper.setProperties(tartarus, "name", "tartarus");

        // edges

        jupiter.addEdge("father", saturn);
        jupiter.addEdge("lives", sky).setProperty("reason", "loves fresh breezes");
        jupiter.addEdge("brother", neptune);
        jupiter.addEdge("brother", pluto);

        neptune.addEdge("lives", sea).setProperty("reason", "loves waves");
        neptune.addEdge("brother", jupiter);
        neptune.addEdge("brother", pluto);

        hercules.addEdge("father", jupiter);
        hercules.addEdge("mother", alcmene);
        ElementHelper.setProperties(hercules.addEdge("battled", nemean), "time", 1, "place", com.thinkaurelius.titan.core.attribute.Geoshape.point(38.1f, 23.7f));
        ElementHelper.setProperties(hercules.addEdge("battled", hydra), "time", 2, "place", com.thinkaurelius.titan.core.attribute.Geoshape.point(37.7f, 23.9f));
        ElementHelper.setProperties(hercules.addEdge("battled", cerberus), "time", 12, "place", com.thinkaurelius.titan.core.attribute.Geoshape.point(39f, 22f));

        pluto.addEdge("brother", jupiter);
        pluto.addEdge("brother", neptune);
        pluto.addEdge("lives", tartarus).setProperty("reason", "no fear of death");
        pluto.addEdge("pet", cerberus);

        cerberus.addEdge("lives", tartarus);

        // commit the transaction to disk
        tx.commit();
    }
}

def getGrandChild(name) {
    me = g.V.has('name',name).next()
    return me.in('father').in('father')
}

def queryBattleWith(name) {
    return g.V.has('name',name).next().query().labels('battled').direction(OUT).orderBy('time',
    com.thinkaurelius.titan.core.Order.DESC).vertices()
}

def queryBattleWithLimit(name, limit) {
    return g.V.has('name',name).next().query().labels('battled').direction(OUT).orderBy('time',
            com.thinkaurelius.titan.core.Order.DESC).limit(limit).vertices()
}

def queryBattleWithConditionLimit(name, time, limit) {
//    return g.V.has('name',name).outE('battled').has('time', T.lt, time).inV[0..<limit]
// Alternative way
    return g.V.has('name',name).next().query().labels('battled').direction(OUT).has('time', Compare.LESS_THAN, time).orderBy('time', com.thinkaurelius.titan.core.Order.DESC).limit(limit).vertices()
}