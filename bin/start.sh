#! /bin/bash
echo "Starting core development server"

(/home/devasia/install/apache-cassandra-2.0.4/bin/stop-server; /home/devasia/install/apache-cassandra-2.0.4/bin/cassandra -p cassandra.pid; lein immutant run)
