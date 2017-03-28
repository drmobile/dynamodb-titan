FROM ubuntu:latest

MAINTAINER Calvin Wu <calvin.wu@soocii.me>

ARG version
ENV CONTAINER_VERSION ${version}

RUN apt-get update
# for add-apt-repository command
RUN apt-get install -y software-properties-common

# install Java
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

# install python 3.3
RUN apt-get install -y python-software-properties
RUN add-apt-repository ppa:fkrull/deadsnakes
RUN apt-get update
RUN apt-get install -y python3.3

RUN apt-get install -y unzip curl

RUN rm -rf /var/lib/apt/lists/*

# download Titan
RUN curl -o /opt/titan.zip https://s3.amazonaws.com/dynamodb-titan-us-east-1/dynamodb-titan054-storage-backend-1.0.1-hadoop2.zip

RUN unzip /opt/titan.zip -d /opt/ && \
    rm /opt/titan.zip

# copy Titan config and run script
ADD rexster-dynamodb.xml.template /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/
ADD run.sh /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/

WORKDIR /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2

EXPOSE 8182
EXPOSE 8183
EXPOSE 8184

# install awslogs agent
ADD awslogs.conf /opt/awslogs/
RUN mkdir -p /etc/cron.d
RUN curl https://s3.amazonaws.com/aws-cloudwatch/downloads/latest/awslogs-agent-setup.py -O
RUN python ./awslogs-agent-setup.py -n --configfile="/opt/awslogs/awslogs.conf" --region=ap-northeast-1
RUN service awslogs stop

CMD ["/opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/run.sh"]