#
# Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
# Portions copyright 2017 JanusGraph authors
#
# Licensed under the Apache License, Version 2.0 (the "License").
# You may not use this file except in compliance with the License.
# A copy of the License is located at
#
#  http://aws.amazon.com/apache2.0
#
# or in the "license" file accompanying this file. This file is distributed
# on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
# express or implied. See the License for the specific language governing
# permissions and limitations under the License.
#

# Modified based on dynamodb-janusgraph-storage-backend/src/test/resources/dynamodb-janusgraph-docker/Dockerfile
FROM openjdk:8

MAINTAINER Calvin Wu <calvin.wu@soocii.me>

ARG version
ARG server_zip
ENV CONTAINER_VERSION ${version}

RUN apt-get update -y && apt-get install -y zip curl

RUN curl https://bootstrap.pypa.io/get-pip.py -O && python get-pip.py

# install JanusGraph
RUN curl -o /var/janusgraph.zip https://s3-ap-northeast-1.amazonaws.com/soocii-resources/${server_zip}
RUN unzip -q /var/janusgraph.zip -d /var
RUN server_base=`basename ${server_zip} .zip` && mv /var/${server_base} /var/janusgraph
RUN rm /var/janusgraph.zip

# install awslogs agent
ADD awslogs.conf /opt/awslogs/
RUN mkdir -p /etc/cron.d
RUN curl https://s3.amazonaws.com/aws-cloudwatch/downloads/latest/awslogs-agent-setup.py -O
RUN python ./awslogs-agent-setup.py -n --configfile="/opt/awslogs/awslogs.conf" --region=ap-northeast-1
RUN service awslogs stop

# download wait-for-it
RUN curl -o /var/janusgraph/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
RUN chmod +x /var/janusgraph/wait-for-it.sh

WORKDIR /var/janusgraph

# add config to docker
ADD run.sh /var/janusgraph/
ADD dynamodb.template.properties /var/janusgraph/conf/gremlin-server
ADD gremlin-server.yaml /var/janusgraph/conf/gremlin-server

EXPOSE 8182
EXPOSE 8183
EXPOSE 8184

CMD ["/var/janusgraph/run.sh"]