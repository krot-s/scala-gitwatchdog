package gitwatchdog.config

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import java.io.FileNotFoundException

class XmlConfigTest extends FlatSpec with ShouldMatchers {
	"XmlConfig" should "throw FileNotFoundException when config file does not exist" in {
	  intercept[FileNotFoundException] { 
	    XmlConfig.read(new File("/non existing file"))
	  }
	}
}