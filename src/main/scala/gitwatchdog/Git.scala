package gitwatchdog
import java.io.File
import java.io.IOException
import scala.sys.process.Process
import scala.sys.process.ProcessLogger
import scala.collection.mutable.ListBuffer
import java.util.Date
import scala.util.matching.Regex
import java.text.SimpleDateFormat

class Git(repository: File) {
	
	def log  = {
	  val repoNormalized = if(repository.getAbsolutePath().endsWith("/"))
		  repository.getAbsolutePath() else repository.getAbsolutePath() + "/"
	  val command = 
	    """git --git-dir=${repo}.git --work-tree=${repo} log --pretty=format:%H---%an---%ai"""
			  .replace("${repo}", repoNormalized)
	  val log = new ListBuffer[LogRecord]
	  val logger = ProcessLogger(line => log += LogRecord.fromString(line, repository))
	  if(Process(command).run(logger).exitValue != 0){
	    throw new IOException("Git command execution failed");
	  }	  

	  log.toList
	}
}

object Git {
  def apply(repository: File) = {
    new Git(repository)
  }
}