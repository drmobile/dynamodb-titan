FROM ubuntu:latest

MAINTAINER Calvin Wu <calvin.wu@soocii.me>

RUN apt-get update
# for add-apt-repository command
RUN apt-get install -y software-properties-common

# install Java
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

# download Titan
RUN apt-get install -y unzip
RUN wget -O /opt/titan.zip https://s3.amazonaws.com/dynamodb-titan-us-east-1/dynamodb-titan054-storage-backend-1.0.1-hadoop2.zip
RUN unzip /opt/titan.zip -d /opt/ && rm /opt/titan.zip

# install python 3.4
RUN apt-get install -y python-software-properties
RUN add-apt-repository ppa:deadsnakes/ppa
RUN apt-get update
RUN apt-get install -y python3.4

# install pip
RUN wget https://bootstrap.pypa.io/get-pip.py -O /tmp/get-pip.py
RUN python /tmp/get-pip.py
RUN pip install bulbs

RUN rm -rf /var/lib/apt/lists/*

# copy Titan config and run script
ADD rexster-dynamodb.xml /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/
ADD run.sh /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/
ADD test.py /tmp/

WORKDIR /opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2

CMD ["/opt/dynamodb-titan054-storage-backend-1.0.1-hadoop2/run.sh"]