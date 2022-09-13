package org.infinispan.wfink.server.task;

import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;;

/**
 * Simple implementation for a server side Task to count the entries for a cache owned by this node.
 *
 * @author <a href="mailto:WolfDieter.Fink@gmail.com">Wolf-Dieter Fink</a>
 *
 */
public class CacheEntryCounterTask implements ServerTask<String> {

  private static final Log log = LogFactory.getLog(CacheEntryCounterTask.class);
  private TaskContext context = null;

  public CacheEntryCounterTask() {
    log.info("CacheEntryCounterTask construction");
  }

  @Override
  public String call() throws Exception {
    log.info("CacheEntryCounterTask called");
    log.info("Subject from context : " + this.context.getSubject());
    Cache<String, String> cache = (Cache<String, String>) context.getCache().get();
    int size = cache.getAdvancedCache().withFlags(Flag.CACHE_MODE_LOCAL).size();
    return "CacheEntryCounterTask cache.size=" + size;
  }

  @Override
  public String getName() {
    log.info("getName() called");
    return "CacheEntryCounterTask";
  }

  @Override
  public void setTaskContext(TaskContext context) {
    log.info("setTaskContext called " + context.toString());
    this.context = context;

  }

  @Override
  public TaskExecutionMode getExecutionMode() {
    return TaskExecutionMode.ALL_NODES;
  }
}
