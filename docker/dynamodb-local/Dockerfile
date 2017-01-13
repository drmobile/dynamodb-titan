FROM ubuntu:latest

MAINTAINER Calvin Wu <calvin.wu@soocii.me>

RUN apt-get update && \
    apt-get -y upgrade

RUN apt-get install -y zip wget default-jdk

WORKDIR /opt/

RUN wget -O /opt/dynamodb-local.zip http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.zip

RUN unzip /opt/dynamodb-local.zip -d /opt/dynamodb-local/ && \
    rm /opt/dynamodb-local.zip

WORKDIR /opt/dynamodb-local/

COPY ./run.sh /opt/dynamodb-local/

EXPOSE 8000

ENTRYPOINT ["/usr/bin/java", "-Djava.library.path=/opt/dynamodb-local/DynamoDBLocal_lib", "-jar", "DynamoDBLocal.jar", "-sharedDb"]