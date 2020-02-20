#!/bin/bash
#first argument is IP a dse node in the cluster
#second argument is password for DB
#third argument is teh datacenter name
java -cp ./target/ConnectionTest-1.0-SNAPSHOT.jar:./target/dependency/* com.datastax.ca.java.ConnectionTest $1 $2 $3
