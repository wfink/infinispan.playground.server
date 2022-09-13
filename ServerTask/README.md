Client Server mode: Execute a server side task 
===============================================
Author: Wolf-Dieter Fink
Level: Beginner
Technologies: Infinispan, Hot Rod, Server side task execution


What is it?
-----------

This example demonstrates how to implement a task running at server side triggered by HotRod client.


Prepare a server instance
-------------
Remove the securityRealm from the <endpoint> element and <security> from the cache container to run without security.
Add a distributed cache to the cache-container

      <distributed-cache name="default"/>

Build and Run the example
-------------------------
1. Type this command to build and deploy the archive:

        mvn clean package

2. Copy the server side application and start the server

       cp server/target/ServerTask.jar $ISPN_HOME/server/lib/
       $ISPN_HOME/bin/server.sh

3. Use Eclipse or run the following to start the test program as Java application

       cd client
       mvn exec:java

4. check the logfiles and response.
   Note the Object returned from the task depends on the task mode ONE_NODE (Simple String) or ALL_NODES (Collection)!

5. You might add additional servers to the cluster to show how the task will be executed on all nodes and the result is a collection of all returned values.
