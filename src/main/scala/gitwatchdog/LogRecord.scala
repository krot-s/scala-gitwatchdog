package gitwatchdog
import scala.util.matching.Regex
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File

class LogRecord(val hash: String, val author: String, val date: Date, val repository: File) {
  override def toString = {
    String.format("Hash = '%s' Author = '%s' Date = '%s'", hash, author, date)
  }
}

object LogRecord {
  def fromString(raw: String, repository: File) = {
    val Format = new Regex("(.*)---(.*)---(.*)")
    val Format(hash, author, date) = raw
    new LogRecord(hash, author, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(date), repository)
  }
}