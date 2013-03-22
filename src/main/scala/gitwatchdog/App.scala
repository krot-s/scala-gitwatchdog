package gitwatchdog

import java.io.File
import gitwatchdog.git.Repository
import gitwatchdog.config.XmlConfig

object App {
  def main(args: Array[String]) {    
    val config = XmlConfig.read(new File(System.getenv("HOME") + "/.gitwatchdog/config.xml"))
    new Watchdog(config.repositories, config.timeout).start    
  }
}
