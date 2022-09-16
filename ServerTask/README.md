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
Use a vanilla server Infinispan 9.4
Add the following security settings to standalone/configuration/clustered.xml

    <cache-container ....>
      <security>
        <authorization>
          <identity-role-mapper/>
          <role name="task" permissions="WRITE READ EXEC"/>
        </authorization>
      </security>
...

      <distributed-cache name="default" owners="1">
        <security>
          <authorization roles="task"/>
       </security>
      </distributed-cache>
....
       <hotrod-connector socket-binding="hotrod" cache-container="clustered">
          <topology-state-transfer lazy-retrieval="false" lock-timeout="1000" replication-timeout="5000"/>
          <authentication security-realm="ApplicationRealm">
             <sasl mechanisms="PLAIN" qop="auth" server-name="server"/>
          </authentication>


Add a user by running the following command :

      bin/add-user.sh -a -p wfink42 -u wfink -g task

Build and Run the example
-------------------------
1. Type this command to build and deploy the archive:

        mvn clean package

2. Copy the server side application and start the server

       cp server/target/ServerTask.jar $ISPN_HOME/standalone/deployments
       $ISPN_HOME/bin/standalone.sh -c clustered.xml

3. Use Eclipse or run the following to start the test program as Java application

       cd client
       mvn exec:java


4. check the logfiles and response.
   Note the Object returned from the task depends on the task mode ONE_NODE (Simple String) or ALL_NODES (Collection)!

5. You might add additional servers to the cluster to show how the task will be executed on all nodes and the result is a collection of all returned values.
