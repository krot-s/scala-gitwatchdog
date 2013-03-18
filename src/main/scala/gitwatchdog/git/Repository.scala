package gitwatchdog.git
import java.io.File

/**
 * Watched GIT repository description. Root is path to repository folder (where '.git' folder is located).
 * Paths - list of relative to root folder paths where commits are watched. 
 */
case class Repository(_root: String, _paths: List[String] = List.empty) {
  val root = new File(_root)
  val paths = _paths
}