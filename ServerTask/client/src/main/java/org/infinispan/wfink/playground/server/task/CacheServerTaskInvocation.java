/**
 *
 */
package org.infinispan.wfink.playground.server.task;

import java.util.List;
import java.util.logging.Logger;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;

/**
 * Add some entries to a cache and invoke the server side task registered as 'CacheEntryCounterTask' to count the entries owned by a node.
 *
 * <p>
 * Because of https://issues.redhat.com/browse/ISPN-14131 there is a need to use JavaSerialization instead of ProtoStream if the task is running in mode ALL_NODES. Otherwise an Exception is thrown. This will not happen if the mode is ONE_NODE as the return value is a simple String.
 * </p>
 * <p>
 * <b>Note</b> if the cache needs other encoding/marshalling it will be not possible to run cache access and task execution with the same client. After ISPN-14131 has been fixed the protostream marshalling will be able to handle the object (List) returned by the task execute().
 * </p>
 *
 * @author <a href="mailto:WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 */
public class CacheServerTaskInvocation {

  private static Logger logger = Logger.getLogger(CacheServerTaskInvocation.class.getName());

  /**
   * @param args
   */
  public static void main(String[] args) {

    ConfigurationBuilder remoteBuilder = new ConfigurationBuilder();
    remoteBuilder.addServers("127.0.0.1:11222");
    remoteBuilder.marshaller(new JavaSerializationMarshaller()); // needed because of ISPN-14131
    remoteBuilder.addJavaSerialAllowList("java.util.*");

    RemoteCacheManager remoteCacheManager = new RemoteCacheManager(remoteBuilder.build(), true);

    // obtain a handle to the remote default cache
    RemoteCache<String, String> customCache = remoteCacheManager.getCache("default");

    customCache.start();

    // this is not intercepted for the first time as we are before calling the remote task
    customCache.put("1", "1");
    customCache.put("2", "2");
    customCache.put("3", "3");
    customCache.put("4", "4");

    Object returnFromTask = customCache.execute("CacheEntryCounterTask", null);
    logger.finest("Object returned by execute is " + returnFromTask.getClass());
    if (returnFromTask instanceof List) {
      System.out.println("The server side task is running in ALL_NODES mode");
    }
    System.out.println("Returned from Task : " + returnFromTask);

    customCache.stop();
    remoteCacheManager.stop();
  }

}
