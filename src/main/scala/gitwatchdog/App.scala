package gitwatchdog

import java.io.File
import gitwatchdog.git.Repository

object App {
  def main(args: Array[String]) {
    // TODO add configuration file
    val watchdog = new Watchdog(List(
      Repository("/home/slava/scala/tmp/", List("f1/1"))
    ), 3)    
    watchdog.start()
  }
}
