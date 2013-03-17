package gitwatchdog

import java.io.File
import java.util.Date
import scala.collection.mutable.HashMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Watchdog(repositories: Seq[File]) {
  private final val TIMEOUT = 1000;

  require({
    repositories.find(x => !x.exists()).isEmpty &&
      !repositories.isEmpty
  })

  private val history = new FileRecordsHistory(new File("/tmp/watchdog"))

  // messages already sent to dbus
  val sent = new HashMap[String, Date]
  
  /**
   * start new thread checking for new commits in infinite loop
   */
  def start() {
    DBus.registerNotificationActionsListener((messageId, actionId) => {
      // add all messages to history when user clicks 'Accept' in notification
      for(repo <- repositories) acceptRecords(repo)
    })

    new Thread(new Runnable() {
      def run() {
        while (true) {
          checkNewCommits()
          Thread.sleep(TIMEOUT)
        }
      }
    }).start()
    
    // run thread 
    java.util.concurrent.Executors.newScheduledThreadPool(1)
    	.scheduleAtFixedRate(new Runnable{
    	  def run(){
    	    println("run")
    	    println(sent)
    	    for(key <- sent.filter((x) => x._2.getTime() < new Date().getTime() - 20*1000*60 ).keys)
    	      sent -= key
    	  }
    	}, 20, 20, TimeUnit.MINUTES);
  }

  
  private def checkNewCommits() {
    for (repo <- repositories.distinct) {
      val unvisited = Git(repo).log.filter(rec => !history.contains(rec))      
      if (!unvisited.isEmpty){
        val message = describe(unvisited, repo)

        // do send same commits to notifications too often        
        if(!sent.contains(message)){
        	DBus.sendNotification(message)
        	sent.put(message, new Date())
        }
      }
    }
  }

  private def describe(records: List[LogRecord], repo: File) = records match {
    case Nil => throw new IllegalArgumentException("Records should not be empty")
    case List(x) => String.format("New commit in %s: %s",
      repo.getAbsolutePath(), x.author)
    case x :: xs => {
      String.format("%s new commits in %s", records.length.toString(), repo.getAbsolutePath())
    }
  }

  /**
   * Add all unvisited records to log
   */
  def acceptRecords(repository: File) {
    history.add(Git(repository).log.filter(rec => !history.contains(rec)))
  }
}
