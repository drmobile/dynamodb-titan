def loadGraphOfTheGodsFactory() {
    //GraphOfTheGodsFactory.load(g)
    com.thinkaurelius.titan.example.GraphOfTheGodsFactory.load(g)
    return 0
}

def getGrandChild(name) {
    me = g.V.has('name',name).next()
    //return me.in('father').in('father').name
    return me.in('father').in('father')
}