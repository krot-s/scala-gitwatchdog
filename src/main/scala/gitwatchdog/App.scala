package gitwatchdog

import java.io.File
import scala.sys.process.ProcessImpl
import scala.sys.process.Process
import scala.sys.process.ProcessLogger

object App {
  
  def main(args : Array[String]) {
	  // TODO add config file
	  val watchdog = new Watchdog(List(
	      new File("/home/vkrot/workspace/pls"),
	      new File("/home/vkrot/workspace/pls")
	  ))
	  val sb = new StringBuilder
	  val logger = ProcessLogger(line => sb.append(line).append("\n") )
	  val returnValue = Process("git help").run(logger).exitValue
	  
	  // git --git-dir=/home/vkrot/workspace/pls/.git --work-tree=/home/vkrot/workspace/pls log --pretty=format:"hash='%H' author='%an' date='%ai'" 0a466ef89b2ad4267bffa7aa1dbbb349614641a3..
	  
	  println(sb.toString)
  }
}
