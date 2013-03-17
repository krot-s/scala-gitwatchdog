package gitwatchdog

import java.io.File

object App {
  def main(args: Array[String]) {
    val watchdog = new Watchdog(List(
      new File("/home/slava/scala/tmp/"),
      new File("/home/slava/git/pls/")))
    watchdog.start()
  }
}
