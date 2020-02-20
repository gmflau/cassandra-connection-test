# ConnectionTest

This project has some simple tests to insert and query data using the open source Cassandra java driver and the round robin policy

### Prerequisites

maven and java 1.8 in the PATH

### build project

sudo ./build.sh

### run project

#./run.sh [node IP] [db password] [datacenter name]
sudo ./run.sh 10.8.0.3 cassandra dc0

first argument is IP a dse node in the cluster
second argument is password for DB
third argument is the datacenter name
