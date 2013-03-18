package gitwatchdog
import java.io.File
import java.io.IOException
import scala.sys.process.Process
import scala.sys.process.ProcessLogger
import scala.collection.mutable.ListBuffer
import java.util.Date
import scala.util.matching.Regex
import java.text.SimpleDateFormat
import gitwatchdog.git.Repository

/**
 * GIT functions.
 */
class Git(repo: Repository) {
	/**
	 * Get GIT commits. 
	 */
    def log = {
      val repoPath = repo.root.getAbsolutePath()
	  val repoNormalized = if(repoPath.endsWith("/"))
		  repoPath else repoPath + "/"
		  
	  val command = 
	    """git --git-dir=${repo}.git --work-tree=${repo} log --pretty=format:%H---%an---%ai -- ${paths}"""
			  .replace("${repo}", repoNormalized)
			  .replace("${paths}", repo.paths.mkString(" "))
			  
	  val log = new ListBuffer[LogRecord]
	  val logger = ProcessLogger(line => log += LogRecord.fromString(line, repo.root))
	  if(Process(command).run(logger).exitValue != 0){
	    throw new IOException("Git command execution failed");
	  }	  

	  log.toList      
    }    
}

object Git {
  def apply(repository: Repository) = {
    new Git(repository)
  }
}