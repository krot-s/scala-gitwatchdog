package gitwatchdog

import java.io.File
import scala.sys.process.Process

class Watchdog(locationsToWatch: Seq[File]) {
	require({
	  locationsToWatch.find(x => !x.exists()).isEmpty
	})
	require({
	  !locationsToWatch.isEmpty
	})
	
	// remove duplicates
	val locations = locationsToWatch.distinct
	
	def start() {
	  //val git = Process
	}
	
	override def toString = {
	  locations.mkString
	}
}