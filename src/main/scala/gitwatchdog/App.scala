package gitwatchdog

import java.io.File
import gitwatchdog.git.Repository

object App {
  def main(args: Array[String]) {
    // TODO add configuration file
    val watchdog = new Watchdog(List(
      new Repository("/home/vkrot/workspace/pls/", List(
//          "mobile", 
//          "dao/src/main/java/com/pls/dao/carrier", 
//          "dao/src/main/java/com/pls/dao/tendering", 
//          "dao/src/main/java/com/pls/dao/ZoneDaoImpl.java"))
	    ""))
    ), 3)    
    watchdog.start()
  }
}
