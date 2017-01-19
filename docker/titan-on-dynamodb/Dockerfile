FROM ubuntu:latest

MAINTAINER Calvin Wu <calvin.wu@soocii.me>

RUN apt-get update
# for add-apt-repository command
RUN apt-get install -y software-properties-common
# add PPA server
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update

RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections

RUN apt-get install -y unzip curl
RUN apt-get install -y oracle-java8-installer

RUN rm -rf /var/lib/apt/lists/*

RUN curl -o /opt/titan.zip https://s3.amazonaws.com/dynamodb-titan-us-east-1/dynamodb-titan054-storage-backend-1.0.1-hadoop2.zip

RUN unzip /opt/titan.zip -d /opt/ && \
    rm /opt/titan.zip

ADD rexster-dynamodb.xml.template /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/
ADD run.sh /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/

WORKDIR /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2

EXPOSE 8182
EXPOSE 8183
EXPOSE 8184

CMD ["/opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/run.sh"]