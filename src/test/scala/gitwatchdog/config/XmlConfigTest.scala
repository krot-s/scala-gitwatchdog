package gitwatchdog.config

import java.io.{File => JFile}
import java.io.FileNotFoundException

import scala.reflect.io.File

import org.scalatest.Finders
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.xml.sax.SAXParseException

class XmlConfigTest extends FlatSpec with ShouldMatchers {
	"XmlConfig" should "throw FileNotFoundException when config file does not exist" in {
	  intercept[FileNotFoundException] { 
	    XmlConfig.read(new JFile("/non existing file"))
	  }
	}
	
	it should "throw SAXParseException when file is empty" in {
	  val file = File.makeTemp(dir = new JFile("/tmp"))	  
	  intercept[SAXParseException] { 	  
		  XmlConfig.read(file.jfile)
	  }
	}
	
	it should "throw SAXParseException when xml structure is not valid" in {
	  val file = File.makeTemp(dir = new JFile("/tmp"))
	  file.writeAll("<xml ")
	  intercept[SAXParseException] { 	  
		  XmlConfig.read(file.jfile)
	  }
	}
	
	it should "parse valid xml" in {
	  val file = tempFileFromString("""
		<config>
		  <timeout>30</timeout>
		  <repository>
		    <root>/home/vkrot/workspace/pls</root>
		    <path>activemq</path>
		    <path>backend</path>  
		  </repository>  
		  <repository>
		    <root>/home/vkrot/scala/scala-gitwatchdog</root>
		    <path>src/main</path>
			<path>src/tests</path>
		    <path>pom.xml</path>  
		  </repository>  
		</config>			  
	  """)
	  
	  val config = XmlConfig.read(file)

	  config.timeout should be (30)
	  config.repositories should have length (2)
	  
	  config.repositories(0).root.getPath() should be ("/home/vkrot/workspace/pls")
	  config.repositories(0).paths should be (List("activemq", "backend"))

	  config.repositories(1).root.getPath() should be ("/home/vkrot/scala/scala-gitwatchdog")
	  config.repositories(1).paths should be (List("src/main", "src/tests", "pom.xml"))
	}
	
	it should "trim all strings" in {
	  val file = tempFileFromString("""
		<config>
		  <timeout>30 </timeout>
		  <repository>
		    <root> /home/vkrot/workspace/pls </root>
		    <path> activemq </path>
			<path>backend </path>
		  </repository>  
		</config>			  
	  """)
	  
	  val config = XmlConfig.read(file)
	  config.timeout should be (30)
	  
	  config.repositories should have size (1)	  
	  config.repositories(0).root.getPath() should be ("/home/vkrot/workspace/pls")
	  config.repositories(0).paths should be (List("activemq", "backend"))
	}
	
	def tempFileFromString(text: String) = {
	  val file = File.makeTemp(dir = new JFile("/tmp"))
	  file.writeAll(text)
	  file.jfile
	}
}