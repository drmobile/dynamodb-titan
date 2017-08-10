# Soocii Dynamodb JanusGraph
Dynamodb JanusGraph docker with customized configuration.

| Branch | Build Status |
|--------|--------------|
| master| [![Build Status](https://travis-ci.org/drmobile/dynamodb-titan.svg?branch=master)](https://travis-ci.org/drmobile/dynamodb-titan)|
| janus| [![Build Status](https://travis-ci.org/drmobile/dynamodb-titan.svg?branch=janus)](https://travis-ci.org/drmobile/dynamodb-titan) |

## Build Dynamodb-JanusGraph (Optional)
If you need to re-build DynamoDB-JanusGraph, follow the steps below to build DynamoDB-JanusGraph.
1. Build package.
    1. Setup environment to compile DynamoDB-JanusGraph. [Reference](https://github.com/awslabs/dynamodb-janusgraph-storage-backend#load-a-subset-of-the-marvel-universe-social-graph)
    2. Run `install-gremlin-server.sh` to distribute Maven package.
    ```console
    cd dynamodb-janusgraph-storage-backend && \
    bash src/test/resources/install-gremlin-server.sh
    ```
2. Upload compiled package from `dynamodb-janusgraph-storage-backend/server/dynamodb-janusgraph-storage-backend-<version>.zip` to S3 `soocii-resources` bucket and mark package as public.
3. Update `server_zip` arg in `docker-compose.yml` and `.travis.yml`.

## Build dockers locally
Run `docker-compose build`

## Test
### Environment
- Python 3.5
- Installed packages
```console
pip install requirement.txt
```
### Run Tests
```console
docker-compose up -d
sleep 30
pytest
docker-compose down
```

## Customize JanusGraph Docker
### Image
soocii/dynamodb-janusgraph

### Init Scripts
Mount your JanusGraph scripts which will build schema and index into docker: `/var/janusgraph/scripts/init.groovy`.
Gremlin server will start and run these script as init scripts.

#### Sample of init.groovy 
```groovy
// an init script that returns a Map allows explicit setting of global bindings.
def globals = [:]

// defines a sample LifeCycleHook that prints some output to the Gremlin Server console.
// note that the name of the key in the "global" map is unimportant.
globals << [hook : [
        onStartUp: { ctx ->
            ctx.logger.info("Executed once at startup of Gremlin Server.")
        },
        onShutDown: { ctx ->
            ctx.logger.info("Executed once at shutdown of Gremlin Server.")
        }
] as LifeCycleHook]

// define the default TraversalSource to bind queries to - this one will be named "g".
globals << [g : graph.traversal()]
```

### Variables
| Name                  | Description                                   | Default                                       |
|-----------------------|-----------------------------------------------|-----------------------------------------------|
| DYNAMODB_HOSTPORT     | DynamoDB endpoint                             |                                               |
| AWS_ACCESS_KEY_ID     | As title.                                     |                                               |
| AWS_SECRET_ACCESS_KEY | As title.                                     |                                               |
| GRAPH_NAME            | Prefix of DynamoDB tables.                    | jg                                            |
| JAVA_OPTIONS          | Java command's options.                       | Chech gremlin-server.sh in JanusGraph package |
| GREMLIN_SERVER_CONF   | Set  the gremlin server configuration to use. | gremlin-server.yaml                           |                                                                                                                                                                                            |