package gitwatchdog
import java.io.File
import scala.io.Source
import java.io.FileWriter
import java.io.Writer
import scala.util.matching.Regex
import scala.collection.mutable.ListBuffer

trait ExaminedRecordsHistory {
  def contains(record: LogRecord): Boolean

  def add(record: LogRecord): Unit

  def add(records: List[LogRecord]): Unit
}

class FileRecordsHistory(file: File) extends ExaminedRecordsHistory {
  require(file.exists() || file.createNewFile())

  private var history: List[(String, String)] = loadHistory

  /**
   * Parse history file and return a list of typles (hash, repository path)
   */
  private def loadHistory = {
    val Format = new Regex("(.*)-(.*)")
    val result = new ListBuffer[(String, String)]()
    for (line <- Source.fromFile(file).getLines()) {      
      val Format(hash, repo) = line      
      result += ((hash, repo))
    } 
    result.toList
  }

  override def contains(record: LogRecord): Boolean = {
    return !history.find(
        x => (record.hash == x._1 && record.repository.getAbsolutePath() == x._2)).isEmpty    
  }

  override def add(records: List[LogRecord]): Unit = {
    using(new FileWriter(file, true)) {
      file =>
        for (rec <- records) file.write(toString(rec) + "\n")
    }
    history = loadHistory	
  }

  override def add(record: LogRecord): Unit = add(List(record))

  private def using(resource: Writer)(f: Writer => Unit) {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }

  private def toString(record: LogRecord) = String.format("%s-%s", record.hash, record.repository.getAbsolutePath())
}