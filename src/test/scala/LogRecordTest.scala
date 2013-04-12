import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import gitwatchdog.LogRecord

class LogRecordTest extends FlatSpec with ShouldMatchers {
	"LogRecord " should " be parsed from valid string" in {
	  import DateUtils._
	  
	  val rec = LogRecord.fromString("sha1hash---authorName---2013-03-15 12:01:12 +0000", new File("/tmp/repository"))
	  rec.author should be ("authorName")
	  rec.hash should be ("sha1hash")
	  year(rec.date) should be (2013)
	  month(rec.date) should be (3)
	  day(rec.date) should be (15)
	  // TODO uncomment and fix timezones
	  // hour(rec.date) should be (12)
	}
	
	it should " throw MatchError when string format is invalid" in {
	  intercept[MatchError] {
		  LogRecord.fromString("ABRACADARBA", new File("/tmp/repository"))
	  }
	}
}