package gitwatchdog

import java.io.File
import java.util.Date
import scala.collection.mutable.HashMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import scala.collection.immutable.List
import git._
import scala.concurrent.ops._
import java.util.concurrent.ScheduledThreadPoolExecutor
import gitwatchdog.gui.Swing3

/**
 * Watches changes periodically in given GIT repositories and sends notifications to KNotify when new

 * commits are seen. Repositories are specified as java.io.File objects pointing to root of repository 
 * (place where '.git' folder resides) along with optional list of paths to check. 
 * For example, when path contains "subfolder", only commits in "subfolder" will be taken into account.
 * Path is relative to git root.
 */
class Watchdog(repositories: Seq[Repository], checkTimeoutSeconds:Long = 2) {
  private final val TWENTY_MINUTES = 20*1000*60
  
  require({
    // check that all give repositories exist
    repositories.map(_.root).find(!_.exists()).isEmpty &&
      !repositories.isEmpty
  })
  
  private val history = new FileRecordsHistory(new File("/tmp/watchdog"))
  
  private val threadPool = java.util.concurrent.Executors.newScheduledThreadPool(1)
  implicit def functionToRunnable(f: () => Unit) = {
    new Runnable { 
      override def run() = f()
    }
  }

  // messages already sent to dbus
  val sent = new HashMap[String, Date]
  
  /**
   * start new thread checking for new commits in infinite loop
   */
  def start() {
    DBus.registerNotificationActionsListener((messageId, actionId) => {
      // add all messages to history when user clicks 'Accept' in notification
      // TODO: extract data from notification which exact notification has been clicked and add
      // only those commits to history
      for(repo <- repositories) acceptRecords(repo)
      Swing3.main(Array.empty[String])
    })
        
    threadPool.scheduleAtFixedRate(
        () => { 
          checkNewCommits() 
        }, 
        0, checkTimeoutSeconds, TimeUnit.SECONDS)

    // clear "sent" collection - remove records older than 20 minutes. run this each second.
    threadPool.scheduleAtFixedRate(
        () => {
    	    for(key <- sent.filter((x) => x._2.getTime() < new Date().getTime() - TWENTY_MINUTES).keys)
    	      sent -= key
    	}, 
    	1, 1, TimeUnit.SECONDS);
  }

  
  private def checkNewCommits() {
    for (repo <- repositories.distinct) {
      Git(repo).fetch
      val unvisited = Git(repo).log.filter(rec => !history.contains(rec))      
      if (!unvisited.isEmpty){
        val message = describe(unvisited, repo.root)

        // do send same commits to notifications too often    
        // TODO: add more elegant solution
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
  private def acceptRecords(repository: Repository) {
    history.add(Git(repository).log.filter(rec => !history.contains(rec)))
    sent.clear()
  }
}
